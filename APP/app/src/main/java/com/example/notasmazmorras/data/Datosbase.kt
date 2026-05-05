package com.example.notasmazmorras.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.data.model.local.LocalCreature
import com.example.notasmazmorras.data.model.local.LocalNote
import com.example.notasmazmorras.data.model.local.LocalObject
import com.example.notasmazmorras.data.model.local.LocalPlace
import com.example.notasmazmorras.data.model.local.LocalUser
import com.example.notasmazmorras.data.model.local.LocalUserRelation
import com.example.notasmazmorras.data.model.local.ScheduleTypeConverter
import com.example.notasmazmorras.data.model.local.SysTable
import com.example.notasmazmorras.data.repositories.daos.CampaignDao
import com.example.notasmazmorras.data.repositories.daos.CharacterDao
import com.example.notasmazmorras.data.repositories.daos.CreatureDao
import com.example.notasmazmorras.data.repositories.daos.NoteDao
import com.example.notasmazmorras.data.repositories.daos.ObjectDao
import com.example.notasmazmorras.data.repositories.daos.PlaceDao
import com.example.notasmazmorras.data.repositories.daos.SysTableDao
import com.example.notasmazmorras.data.repositories.daos.UserDao
import com.example.notasmazmorras.data.repositories.daos.UserRelationDao

@Database(entities = [

    LocalCampaign::class,
    LocalCharacter::class,
    LocalCreature::class,
    LocalNote::class,
    LocalObject::class,
    LocalPlace::class,
    LocalUser::class,
    LocalUserRelation::class,
    SysTable::class,

], version = 6)
@TypeConverters(value = [ScheduleTypeConverter::class])
abstract class Datosbase : RoomDatabase() {

    abstract fun campaignDao() : CampaignDao
    abstract fun characterDao() : CharacterDao
    abstract fun creatureDao() : CreatureDao
    abstract fun noteDao() : NoteDao
    abstract fun objectDao() : ObjectDao
    abstract fun placeDao() : PlaceDao
    abstract fun userDao() : UserDao
    abstract fun userRelationDao() : UserRelationDao
    abstract fun sysTableDao() : SysTableDao

    companion object {

        @Volatile
        private var Instance : Datosbase? = null

        fun getDatabase(context : Context) : Datosbase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    Datosbase::class.java,
                    "dnd_database"
                ).fallbackToDestructiveMigration().build().also { Instance = it }
            }
        }

    }

}