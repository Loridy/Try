(ns try.core
  (:gen-class)
  (:require [clojure.set :as set]))

(require  '[clojure.string :as str :refer [split trim]])
(require '[clojure.set :as set])
(use 'clojure.java.io)
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


;; You may use following command to try
;; (get-delimiter "resources/try.csv")


(defn two-chars-delimiters
  "take a string, and get two-character delimiters"
  [string]
  (loop [two {} start 0]
    (if (= start (- (count string) 2))
      (assoc two (subs string start (+ start 2)) (count (split string (re-pattern (subs string start (+ start 2))))))
      (recur
       (assoc two (subs string start (+ start 2)) (- (count (split string (re-pattern (subs string start (+ start 2))))) 1))
       (inc start)))))

(defn three-chars-delimiters
  "take a string, and get three-character delimiters"
  [string]
  (loop [three {} start 0]
    (if (= start (- (count string) 3))
      (assoc three (subs string start (+ start 3)) (count (split string (re-pattern (subs string start (+ start 3))))))
      (recur
       (assoc three (subs string start (+ start 3)) (- (count (split string (re-pattern (subs string start (+ start 3))))) 1))
       (inc start)))))

(defn all-possible-delimiters
  "get all possible delimiters (1&2&3 charaters)"
  [string]
  (merge
   (frequencies string)
   (two-chars-delimiters string)
   (three-chars-delimiters string)))


(defn check-same-val
  "Used to get pairs with the same keys and vals"
  [d1 d2]
  (loop [d {} k (keys d1)]
    (if (= (count k) 0)
      d
      (do
        (let [kk (first k)]
          (if (= (get d1 kk) (get d2 kk))
            (recur (assoc d kk (get d1 kk)) (rest k))
            (recur d (rest k))))))))

(def illegal-char [\" \( \)])
;; exclude both quote notation and parenthesis as parenthesis sometimes create errors

(defn ignore-quotes 
  "ignore information inside quote notation"
  [string]
  (loop [len 0 new-string "" inquote? false]
    (if (= len (count string))
      new-string
      (do
        (if (not
             (.contains illegal-char (nth string len))
            ;;  (= \" (nth string len))
             )
          (do
            (if inquote?
              (recur (inc len) new-string inquote?)
              (recur (inc len) (str new-string (nth string len)) inquote?)))
          (recur (inc len) new-string (not inquote?)))))))

;; To be updated: take key-val pair into consideration
(defn check-terminate 
  "current termination rule is that the result dictionary only contains
   delimiters that are sub-string or super-string of others"
  [ks]
  (def terminate? (atom true))
  (doseq [kk ks
          kkk ks]
    (if
     (not ((fn [s1 s2] (or (.contains s1 s2) (.contains s2 s1))) (str (first kk)) (str (first kkk))))
      (reset! terminate? false)))
  @terminate?)

;; to get possible delimiter from a file
;; terminate when number of possible delimiters is smaller than 6 and satisfies above termination rule
(defn get-delimiter [filename]
  (let [rdr (reader filename)]
    (loop [line 0 result (all-possible-delimiters (ignore-quotes (.readLine rdr)))]
      (if
       (and (< (count result) 6) (check-terminate result))
        (println result)
        (do
          (let [next-line (all-possible-delimiters (ignore-quotes (.readLine rdr)))]
            (recur (inc line) (check-same-val result next-line))))))))



;; (time (do
;;         ))