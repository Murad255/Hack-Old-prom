package com.example.mqttkotlinsample.data


class OrderStatus(orderStatusID: Int,
                  data: String,time: String, status: String,place: String,
                  isCompliteStatus: Int  = 1){
    var OrderStatusID: Int=orderStatusID
    var Data: String = data
    var Time: String = time
    var Status: String = status
    var Place: String = place
    var IsCompliteStatus: Int = isCompliteStatus // 0 - доставка завершена, 1 - идёт доставка, 2 - принят на обработку, 3 - ошибка
}