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

(defn factorial [n]
  (if (= n 0)
    1
    (* n (factorial (dec n)))))

;; TODO:
;; indicate the seperator for a data file
;; eg, ','/', ' in .csv

;; re-pattern
;; split

;; (with-open [rdr (reader "resources/try.csv")]
;;   (let [line (line-seq rdr)]
;;     (println line)))

;; (defn meaningful? [chr]
;;   (println chr)
;;   (println (int chr))
;;   (and (> 47 (int chr)) (< 58 (int chr))))

;; (defn is_meaningful_character [chr]
;;   ;; (println (str chr ": " (int chr)))
;;   (or
;;    (and (<= 48 (int chr)) (>= 57 (int chr)))
;;    (and (<= 65 (int chr)) (>= 90 (int chr)))
;;    (and (<= 97 (int chr)) (>= 122 (int chr)))))

;; (defn meaningful? [string]
;;   (def is_meaningful? (atom false))
;;   (doseq [chr string]
;;     ;; (print (str chr ": "))
;;     ;; (println (int chr))
;;     ;; (println (is_meaningful_character chr))
;;     (cond (not (is_meaningful_character chr)) (reset! is_meaningful? false)))
;;   (not @is_meaningful?))


;; (with-open [rdr (reader "resources/try.csv")]
;;   (println (.readLine rdr)))

;; (let [rdr (reader "resources/try.csv")]
;;   (println (.readLine rdr)))

;; (let [rdr (reader "resources/try.csv")]
;;   (println (nth (line-seq rdr) 1)))


(defn two_chars_delimiters [string]
  (loop [two #{} start 0]
    (if (= start (- (count string) 1))
      two
      (recur (conj two (subs string start (+ start 2))) (inc start)))))

(defn three_chars_delimiters [string]
  (loop [three #{} start 0]
    (if (= start (- (count string) 2))
      three
      (recur (conj three (subs string start (+ start 3))) (inc start)))))



;; (defn read_lines [name]
;;   (loop [count 0 delimiters #{}]
;;     (if (= count 10)
;;       delimiters
;;       (recur (inc count) (-> delimiters
;;                              (conj))))))

(defn get_possible_delimiters [string]
  (reduce conj (two_chars_delimiters string) (three_chars_delimiters string)))


;; (def data {\a 1 \A 2})
;; (apply max-key #(val (first %)) data)
;; (apply max-key #(val (first (vec %))) data)

;; (filter #(= (second %) (reduce max (vals data))) data)

(defn get_max_delimiter [data]
  (filter #(= (second %) (reduce max (vals data))) data))

;; (defn read_first_line [filename]
;;   (loop [rdr (reader filename)]
;;     (if (nil? (first (line-seq rdr)))
;;       (println "haha")
;;       (recur (rest (line-seq rdr))))))

(defn read_first_line [filename]
  (let [rdr (reader filename)]
    (println (first (line-seq rdr)))))

;; (defn read_lines [filename]
;;   (let [rdr (reader filename)]
;;     (loop [count 0]
;;       (when (< count 10)
;;         (println (get_max_delimiter (frequencies (.readLine rdr))))
;;         (recur (inc count))))))
;; ########################################################################################



;; (defn all_possible_delimiters [string]
;;   (reduce conj 
;;           (set (keys (frequencies string))) 
;;           (two_chars_delimiters string) 
;;           (three_chars_delimiters string)))

;; (defn all_possible_delimiters [string]
;;   (reduce conj 
;;           (two_chars_delimiters string)
;;           (three_chars_delimiters string)))


;; (defn read_lines [filename]
;;   (let [rdr (reader filename)]
;;     (loop [line 0 delimiters (all_possible_delimiters (.readLine rdr))]
;;       (when (< line 100)
;;         ;; (println (get_max_delimiter (frequencies (.readLine rdr))))
;;         (println (str "line" line ": " delimiters "\n"))
;;         ;; (println delimiters)
;;         (recur (inc line) (set/intersection delimiters (all_possible_delimiters (.readLine rdr))))))))


;; (defn read_lines [filename]
;;   "get only the final possible result"
;;   (let [rdr (reader filename)]
;;     (loop [line 0 delimiters (all_possible_delimiters (.readLine rdr))]
;;       (if (or (= line 100) (< (count delimiters) 6))
;;         ;; (println (get_max_delimiter (frequencies (.readLine rdr))))
;;         ;; (println (str "line" line ": " delimiters "\n"))
;;         delimiters
;;         (recur (inc line) (set/intersection delimiters (all_possible_delimiters (.readLine rdr))))))))



;; (let [rdr (reader "resources/8001.txt")]
;;   (loop [x 0]
;;     (when (< x 100)
;;       (println (all_possible_delimiters (.readLine rdr)))
;;       (recur (inc x)))))


;; (filter #(>= (second %) (nth (reverse (sort (vals ss))) 5)) ss)


(defn read_lines [filename]
  (let [rdr (reader filename)]
    (loop [line 0 d_frequency ss]
      (if (= line 1)
        (println d_frequency)
        (recur (inc line) ())))))



(defn two_chars_delimiters
  "take a string, and get two-character delimiters"
  [string]
  (loop [two {} start 0]
    (if (= start (- (count string) 2))
      (assoc two (subs string start (+ start 2)) (count (split string (re-pattern (subs string start (+ start 2))))))
      (recur
       (assoc two (subs string start (+ start 2)) (- (count (split string (re-pattern (subs string start (+ start 2))))) 1))
       (inc start)))))

(defn three_chars_delimiters
  "take a string, and get three-character delimiters"
  [string]
  (loop [three {} start 0]
    (if (= start (- (count string) 3))
      (assoc three (subs string start (+ start 3)) (count (split string (re-pattern (subs string start (+ start 3))))))
      (recur
       (assoc three (subs string start (+ start 3)) (- (count (split string (re-pattern (subs string start (+ start 3))))) 1))
       (inc start)))))

(defn all_possible_delimiters
  "get all possible delimiters (1/2/3 charaters)"
  [string]
  (merge
   (frequencies string)
   (two_chars_delimiters string)
   (three_chars_delimiters string)))


(defn check
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

(defn ignore_quotes
  "return a string with not quotes"
  [string]
  (loop [len 0 new_string "" inquote? false]
    (if (= len (count string))
      new_string
      (do
        (if (not (= \" (nth string len)))
          (do
            (if inquote?
              (recur (inc len) new_string inquote?)
              (recur (inc len) (str new_string (nth string len)) inquote?)))
          (recur (inc len) new_string (not inquote?)))))))

(def illegal_char [\" \( \)])

(defn ignore_quotes [string]
  (loop [len 0 new_string "" inquote? false]
    (if (= len (count string))
      new_string
      (do
        (if (not
             (.contains illegal_char (nth string len))
            ;;  (= \" (nth string len))
             )
          (do
            (if inquote?
              (recur (inc len) new_string inquote?)
              (recur (inc len) (str new_string (nth string len)) inquote?)))
          (recur (inc len) new_string (not inquote?)))))))

(defn double-check [s1 s2]
  (or (.contains s1 s2) (.contains s2 s1)))

(defn check_terminate [ks]
  (def terminate? (atom true))
  (doseq [kk ks
          kkk ks]
    (if
     (not ((fn [s1 s2] (or (.contains s1 s2) (.contains s2 s1))) (str (first kk)) (str (first kkk))))
      (reset! terminate? false)))
  @terminate?)

;; Problem: for two-char delimiter "n,", doseq will regard is as n rather than

;; (defn check_lines [filename]
;;   (let [rdr (reader filename)]
;;     (loop [line 0 result (all_possible_delimiters (ignore_quotes (.readLine rdr)))]
;;       (if
;;       ;;  (or (< (count result) 6) (= line 10))
;;        (< (count result) 6)
;;         (println result)
;;         (do
;;           (let [next_line (all_possible_delimiters (ignore_quotes (.readLine rdr)))]
;;             (recur (inc line) (check result next_line))))))))

(defn check_lines [filename]
  (let [rdr (reader filename)]
    (loop [line 0 result (all_possible_delimiters (ignore_quotes (.readLine rdr)))]
      (if
       (and (< (count result) 6) (check_terminate result))
        (println result)
        (do
          (let [next_line (all_possible_delimiters (ignore_quotes (.readLine rdr)))]
            (recur (inc line) (check result next_line))))))))

(check_lines "resources/0.csv")

(defn read_first_line [filename]
  (let [rdr (reader filename)]
    (println (nth (line-seq rdr) 1))))

(def s (let [rdr (reader "resources/0.csv")] (nth (line-seq rdr) 1)))

(read_first_line "resources/0.csv")


;; (defn check_terminate [ks]
;;   (loop [kf (first ks) kr (rest ks) terminate? true]
;;     (if (or (nil? kf) (not terminate?))
;;       terminate?
;;       (do 
;;         (loop [t 0 terminate? terminate?]
;;           (if (or (= t (count kr)) (not terminate?))
;;             terminate?
;;             (recur )))))))


;; .contains
;; (defn double_check [s1 s2] 
;;   (or (.contains s1 s2) (.contains s2 s1)))


;; (defn ignore_quotes [string]
;;   (loop [len 0 new_string "" inquote? false]
;;     (if (= len (count string))
;;       new_string
;;       (do
;;         (if (= \" (nth string len))
;;           (do
;;             (println "is quote")
;;             (if inquote?
;;               (recur (inc len) new_string (not inquote?))
;;               (recur (inc len) new_string (not inquote?))))
;;           (do
;;             (if inquote?
;;               (recur (inc len) new_string inquote?)
;;               (recur (inc len) (str new_string (nth string len)) inquote?))))))))




;; (time (do
;;         ))

