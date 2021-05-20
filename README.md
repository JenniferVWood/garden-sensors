# garden-sensors
Sensor-data ingestion for gardening and related

Currently supports a single sensor (ambient temperature)

I chose Grafana as the UI because all I really wanted to do was look at graphs and set alerts, and Grafana does that for me out of the box -- no SPA or static webpage necessary!

#### Data flow:
arduino -> mqtt -> springboot/kotlin -> postgresql -> grafana

#### TODO
* take advantage of koroutines in the MQTT listener, because what if I had 10,000 gardens?
* allow sensor to accept modifications to temparature calibration constants from incoming MQTT
* secure MQTT (shhh.... don't tell, but anyone can totally seee if my garden is running hot or cold)

#### Notes
I've written something very similar to the kotlin piece, in Go, previously.  One of my goals here is to apply my experience with goroutines, and for that matter WebFlux concurrency abstractions, to koroutines.  I'm always looking for new ways to solve the same problem!

![Garden Sensor 0.1](https://github.com/JenniferVWood/garden-sensors/blob/main/PXL_20210503_133640376.jpg)
