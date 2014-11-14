(ns weather-data.rainiest-period-test
  (:require [clojure.test :refer :all])
  (:use [weather-data.rainiestperiod :refer :all]))

(deftest test-rainy
  (testing "rainy returns true for raininess."
    (let [x ["RØLDAL" "01.01.1960" "-" "-" "-" "19.9" "25" "4" "Regn.snø.sludd"]]
    (is (= true (rainy x))))))

(deftest test-add-to-period
  (testing "is able to build rainy period."
    (let [x [1]
          y [2]]
    (is (= (addToLastPeriod [[x]] y)
           [[x y]])))))

(= (addToLastPeriod [[[1]]] 2) [[[1] [2]]])

(deftest test-add-new-period
  (testing "is able to build new rainy period."
    (let [x [1]
          y [2]
          z [3]]
    (is (=
         (createNewPeriod (addToLastPeriod [[[x]]] y) z)
           [[[x y]] [[z]]])))))



(run-tests 'weather-data.rainiest-period-test)
