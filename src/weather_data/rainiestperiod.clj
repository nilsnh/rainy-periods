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

(defn addToLastPeriod [x y]
  [(conj (butlast x) (conj (last x) y))])

(defn addToEmptyPeriod [x y]
  [(conj (butlast x) (conj (last (last x)) y))])

(addToLastPeriod [[[1]]] [2])
(addToEmptyPeriod [[[]]] [2])

(defn createNewPeriod [x y]
  (conj x [y]))

(createNewPeriod [[[1] [2]]] [3])

;Create new periods if x y is different places. Or not raining
(defn noCreatedPeriods [x] (empty? (last (last x))))

(empty? (last (last [[[]]])))

(def rolldal ["RØLDAL" "01.01.1960" "-" "-" "-" "19.9" "25" "4" "Regn.snø.sludd"])
(def moss ["MOSS" "01.01.1960" "-" "-" "-" "19.9" "25" "4" "Regn.snø.sludd"])

(defn lastEntry [x] (last (last x)))

(defn newPlace [x y] (= (location (lastEntry x))
                        (location y)))

(location (lastEntry [[rolldal]]))

(newPlace [[rolldal]] moss)

(defn compareFunc [x y]
  (do (println (str "processing" (lastEntry x))
  (if(noCreatedPeriods x)
    (do
      (println "add to empty period")
      (println (str "created:" (addToEmptyPeriod x y)))
      (addToEmptyPeriod x y))
    ;if new place and raining start building new period
    (if(and (rainy (lastEntry x)) (rainy y))
      (do (println "They're consecutive rainy days.")(addToLastPeriod x y))
      ;if it's not consecutive and raining create new period
      (if(and (not (consecutiveDay (lastEntry x) y)) (rainy y))
        (do
          (println "not consecutive, but raining")
          (createNewPeriod x y)) x))))))

(defn parseTime [entry]
  (let [custom-formatter (f/formatter "dd.MM.YYYY")]
    (f/parse custom-formatter (nth entry 1))))

(defn consecutiveDay [entry1 entry2]
  (= (t/plus (parseTime entry1) (t/days 1)) (parseTime entry2)))

(def data [["RØLDAL" "01.01.1960" "-" "-" "-" "19.9" "25" "4" "Regn.snø.sludd"]
  ["RØLDAL" "02.01.1960" "-" "-" "-" "19.9" "25" "4" "Regn.snø.sludd"]
  ["RØLDAL" "05.01.1960" "-" "-" "-" "19.9" "25" "4" "Regn.snø.sludd"]])

(def data (lazy-seq [["RØLDAL" "01.01.1960" "-" "-" "-" "19.9" "25" "4" "Regn.snø.sludd"]
  ["RØLDAL" "02.01.1960" "-" "-" "-" "15" "25" "4" "Regn.snø.sludd"]
  ["RØLDAL" "03.01.1960" "-" "-" "-" "19.9" "25" "4" "Regn.snø.sludd"]]))

(reduce compareFunc [[[]]] data)

(consecutiveDay (first data) (second data))
