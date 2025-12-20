package com.techrent.app.data.local

import androidx.room.TypeConverter
import com.techrent.app.domain.model.ItemType
import com.techrent.app.domain.model.OrderStatus
import com.techrent.app.domain.model.OrderType
import com.techrent.app.domain.model.Role

class Converters {
    @TypeConverter fun roleToString(v: Role) = v.name
    @TypeConverter fun stringToRole(v: String) = Role.valueOf(v)

    @TypeConverter fun itemTypeToString(v: ItemType) = v.name
    @TypeConverter fun stringToItemType(v: String) = ItemType.valueOf(v)

    @TypeConverter fun statusToString(v: OrderStatus) = v.name
    @TypeConverter fun stringToStatus(v: String) = OrderStatus.valueOf(v)

    @TypeConverter fun orderTypeToString(v: OrderType) = v.name
    @TypeConverter fun stringToOrderType(v: String) = OrderType.valueOf(v)
}
