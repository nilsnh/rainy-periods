(ns weather-data.rainiestperiod-test
  (:require [clojure.test :refer :all][weather-data.rainiestperiod :refer :all :as weather]))

(deftest add-1-to-1
  (is (= 2 (+ 1 1))))

(deftest testAddToLastPeriod
  (let [x [[["entry1"]["entry2"]]]
        y ["entry3"]
        result [[["entry1"]["entry2"]["entry3"]]]]
    (is (= (weather/addToLastPeriod x y) result))))

(deftest testCreateNewPeriod
  (let [x [[["entry1"]["entry2"]]]
        y ["entry3"]
        result [[["entry1"]["entry2"]][["entry3"]]]]
    (is (= (weather/createNewPeriod x y) result))))

(deftest testConsecutiveDay
  (let [x [[["RØLDAL" "26.10.2014" "-" "-" "-" "42.7" "0" "0" "Regn"]]]
        y ["RØLDAL" "27.10.2014" "-" "-" "-" "57.0" "0" "0" "Regn"]]
    (is (weather/consecutiveDay (last (last x)) y))))


(def initData (take 5 (weather/rainyEntries weather/rolldal)))
(def result [[["RØLDAL" "01.01.1960" "-" "-" "-" "19.9" "25" "4" "Regn.snø.sludd"]
              ["RØLDAL" "02.01.1960" "-" "-" "-" "2.0" "19" "4" "Regn"]]
             [["RØLDAL" "05.01.1960" "-" "-" "-" "3.0" "15" "4" "Regn"]
              ["RØLDAL" "06.01.1960" "-" "-" "-" "1.4" "17" "4" "Regn.snø.sludd"]
              ["RØLDAL" "07.01.1960" "-" "-" "-" "0.2" "15" "4" "Regn"]]])

(deftest testRainyPeriods
  (is (= (weather/rainyPeriods initData) result)))

(run-tests)
