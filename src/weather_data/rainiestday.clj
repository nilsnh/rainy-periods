
(ns weather-data.rainiestday
  (:require [clojure.data.csv :as csv]
         [clojure.java.io :as io]))

(def csv
  (with-open [in-file (io/reader "resources/weather.csv")]
  (doall
    (csv/read-csv in-file))))

(defn convertToNum [x]
  (try (Double. x)
    (catch Exception e 0)))

(defn compareFunc [x y]
  (letfn [(rainLevel [entry] (convertToNum (nth entry 5)))]
    (if(> (rainLevel x) (rainLevel y)) x y)))

(reduce compareFunc csv)

