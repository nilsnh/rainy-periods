; Finn mest regnfulle dag,
; lengste periode med regn

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

(defn rainLevel [entry] (convertToNumeric (nth entry 5)))

(defn location [entry] (nth entry 0))

(defn rainy [entry]
  (if (> (rainLevel entry) 0) true false))

(defn parseTime [entry]
  (let [custom-formatter (f/formatter "dd.MM.YYYY")]
    (f/parse custom-formatter (nth entry 1))))

(defn consecutiveDay [entry1 entry2]
  (or (= (t/plus (parseTime entry1) (t/days 1)) (parseTime entry2))
      (= (t/minus (parseTime entry1) (t/days 1)) (parseTime entry2))))

(def listOfPlaces (partition-by (fn [entry] (nth entry 0)) (rest csv)))

(def rolldal (nth listOfPlaces 0))

(defn rainyEntries [place] (filter #(> (convertToNumeric (rainLevel %)) 0) place))

(defn addToLastPeriod [x y]
  (vec (conj (butlast x) (vec (conj (last x) y)))))

(defn createNewPeriod [x y]
  (vec (conj x [y])))

(defn rainyPeriods [raindata] (reduce
 (fn [x y]
   (if(= [[[]]] x)
     (addToLastPeriod (last (last x)) y)
     (if(consecutiveDay (last (last x)) y)
       (do (println "addingToLastperiod")
         (addToLastPeriod x y))
        (createNewPeriod x y))))
 [[[]]]
 raindata))

(rainyPeriods (take 5 (rainyEntries rolldal)))
