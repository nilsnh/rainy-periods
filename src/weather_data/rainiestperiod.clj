(ns weather-data.rainiestperiod
  (:require [clojure.data.csv :as csv]
         [clojure.java.io :as io]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(def csv
  (with-open [in-file (io/reader "resources/weather.csv")]
  (doall
    (csv/read-csv in-file))))

(defn convert-to-double [x]
  (try (Double. x)
    (catch Exception e 0)))

(defn parse-time [entry]
  (let [custom-formatter (f/formatter "dd.MM.YYYY")]
    (f/parse custom-formatter (nth entry 1))))

(defn consecutive-day? [entry1 entry2]
  (or (empty? entry1)
   (= (t/plus (parse-time entry1) (t/days 1)) (parse-time entry2))
   (= (t/minus (parse-time entry1) (t/days 1)) (parse-time entry2))))

(defn filter-rain [place]
  (letfn [(get-rain-level [entry] (convert-to-double (nth entry 5)))]
   (filter #(> (convert-to-double (get-rain-level %)) 0) place)))

(def rain-observations-by-place
  (letfn [(location [entry] (nth entry 0))]
    (partition-by location (filter-rain (rest csv)))))

(defn find-rainy-periods
  "Builds nested list of rainy periods: [[[<-entry->]<-rainyperiods->]]"
  ([remaining] (find-rainy-periods [[(first remaining)]] (rest remaining)))
  ([result remaining]
  (letfn [(add-to-lastperiod [result entry] (conj (pop result) (conj (peek result) entry)))
          (create-new-period [result entry] (conj result [entry]))]
    (if(empty? remaining) result
      (if(consecutive-day? (peek (peek result)) (first remaining))
        (recur (add-to-lastperiod result (first remaining)) (rest remaining))
        (recur (create-new-period result (first remaining)) (rest remaining)))))))

(def rainy-periods (map find-rainy-periods rain-observations-by-place))

(defn find-rainiest-period [periods]
  (let [rainiest-periods (map #(last (sort-by count %)) periods)]
    (last (sort-by count rainiest-periods))))

(find-rainiest-period rainy-periods)

