(ns weather-data.rainiestperiod
  (:require [clojure.data.csv :as csv]
         [clojure.java.io :as io]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(def csv
  (with-open [in-file (io/reader "resources/weather.csv")]
  (doall
    (csv/read-csv in-file))))

(defn convertToNumeric [x]
  (try (Double. x)
    (catch Exception e 0)))

(defn parseTime [entry]
  (let [custom-formatter (f/formatter "dd.MM.YYYY")]
    (f/parse custom-formatter (nth entry 1))))

(defn consecutiveDay [entry1 entry2]
  (or (empty? entry1)
   (= (t/plus (parseTime entry1) (t/days 1)) (parseTime entry2))
   (= (t/minus (parseTime entry1) (t/days 1)) (parseTime entry2))))

(defn filterRain [place]
  (letfn [(getRainLevel [entry] (convertToNumeric (nth entry 5)))]
   (filter #(> (convertToNumeric (getRainLevel %)) 0) place)))

(def rainObservationsByPlace
  (letfn [(location [entry] (nth entry 0))]
    (partition-by location (filterRain (rest csv)))))

(defn findRainyPeriods
  "Builds nested list of rainy periods: [[[<-entry->]<-rainyperiods->]]"
  ([remaining] (findRainyPeriods [[(first remaining)]] (rest remaining)))
  ([result remaining]
  (if(empty? remaining) result
    (if(consecutiveDay (last (last result)) (first remaining))
      (recur (conj (vec (butlast result)) (conj (last result) (first remaining))) (rest remaining))
      (recur (conj result [(first remaining)]) (rest remaining))))))

(def rainyPeriods (map findRainyPeriods rainObservationsByPlace))

(defn findRainiestPeriod [periods]
  (let [rainiestPeriods (map #(last (sort-by count %)) periods)]
    (last (sort-by count rainiestPeriods))))

(findRainiestPeriod rainyPeriods)

