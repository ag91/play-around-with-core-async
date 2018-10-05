(require '[clojure.core.async :as async])

(def a-channel (async/chan 1))

(async/>!! a-channel "Hello World!")

(+ 1 1)

(async/<!! a-channel)

(async/chan 10) ;; this maintains 10 entries

(async/>!! a-channel "Hello 1")
(future (async/>!! a-channel "Hello 2"))
(async/<!! a-channel)

(async/<!! a-channel)

(async/go (async/>! a-channel "hello go block"))
(async/<!! (async/go (async/<! a-channel)))

(let [n 1000
      cs (repeatedly n async/chan)
      begin (System/currentTimeMillis)]
  (doseq [c cs] (async/go (async/>! c "hi")))
  (dotimes [i n]
    (let [[v c] (async/alts!! cs)] ; combines 1000 channels
      (assert (= "hi" v))))
  (println "Read" n "msgs in" (- (System/currentTimeMillis) begin) "ms"))
