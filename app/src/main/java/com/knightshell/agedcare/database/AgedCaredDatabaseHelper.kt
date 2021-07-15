package com.knightshell.agedcare.database

import android.R.attr.entries
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.utils.EntryXComparator
import com.knightshell.agedcare.model.*
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.COLUMN_GLUCOSE_DATE
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.COLUMN_GLUCOSE_GLUCOSE
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.COLUMN_GLUCOSE_TIME
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.COLUMN_PRESSURE_DATE
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.COLUMN_PRESSURE_DIASTOLIC
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.COLUMN_PRESSURE_SYSTOLIC
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.COLUMN_PRESSURE_TIME
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.COLUMN_WEIGHT
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.COLUMN_WEIGHT_DATE
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.COLUMN_WEIGHT_TIME
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.GLUCOSE_TABLE_NAME
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.MONTHLY_PRESSURE_TABLE_NAME
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.MONTHLY_PRESSURE_VALUE
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.PRESSURE_TABLE_NAME
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.WEEKLY_PRESSURE_TABLE_NAME
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.WEEKLY_PRESSURE_VALUE
import com.knightshell.agedcare.model.AgedCareSchema.DatabaseSchemaEntry.Companion.WEIGHT_TABLE_NAME
import java.util.*
import kotlin.collections.ArrayList


class AgedCaredDatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){



    var TAG: String = "AgedCareDatabase"

    fun queryAllPressure(): ArrayList<PressureModel>  {
        val db = this.readableDatabase
        val useList = ArrayList<PressureModel>()
        val cursor = db.rawQuery("SELECT * FROM pressure", null)

        var date: String
        var time: String
        var diastolic: Int
        var systolic: Int
        while (cursor.moveToNext()) {
            date = cursor.getString(cursor.getColumnIndex(COLUMN_PRESSURE_DATE))
            time = cursor.getString(cursor.getColumnIndex(COLUMN_PRESSURE_TIME))
            diastolic = cursor.getInt(cursor.getColumnIndex(COLUMN_PRESSURE_DIASTOLIC))
            systolic = cursor.getInt(cursor.getColumnIndex(COLUMN_PRESSURE_SYSTOLIC))
            useList.add(PressureModel(date, time, diastolic, systolic))
            cursor.moveToNext()
        }
        return useList
    }

    fun queryAllWeight(): ArrayList<WeightModel>  {
        val db = this.readableDatabase
        val useList = ArrayList<WeightModel>()
        val cursor = db.rawQuery("SELECT * FROM weight", null)

        var date: String
        var time: String
        var weight: Float
        while (cursor.moveToNext()) {
            date = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT_DATE))
            time = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT_TIME))
            weight = cursor.getFloat(cursor.getColumnIndex(COLUMN_WEIGHT))

            useList.add(WeightModel(date, time, weight))
            cursor.moveToNext()
        }
        return useList
    }

    fun queryAllGlucose(): ArrayList<GlucoseModel>  {
        val db = this.readableDatabase
        val useList = ArrayList<GlucoseModel>()
        val cursor = db.rawQuery("SELECT * FROM glucose", null)

        var date: String
        var time: String
        var glucose: Float
        while (cursor.moveToNext()) {
            date = cursor.getString(cursor.getColumnIndex(COLUMN_GLUCOSE_DATE))
            time = cursor.getString(cursor.getColumnIndex(COLUMN_GLUCOSE_TIME))
            glucose = cursor.getFloat(cursor.getColumnIndex(COLUMN_GLUCOSE_GLUCOSE))

            useList.add(GlucoseModel(date, time, glucose))
            cursor.moveToNext()
        }
        return useList
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_PRESSURE_TABLE)
        db.execSQL(CREATE_GLUCOSE_TABLE)
        db.execSQL(CREATE_WEIGHT_TABLE)
        db.execSQL("CREATE TABLE IF NOT EXISTS " + AgedCareSchema.DatabaseSchemaEntry.WEEKLY_PRESSURE_TABLE_NAME + " (" +
                        AgedCareSchema.DatabaseSchemaEntry.WEEKLY_PRESSURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        AgedCareSchema.DatabaseSchemaEntry.WEEKLY_PRESSURE_VALUE + " REAL, " +
                        AgedCareSchema.DatabaseSchemaEntry.WEEKLY_CREATED_AT + " DEFAULT CURRENT_TIMESTAMP);")

        db.execSQL("CREATE TABLE IF NOT EXISTS " + AgedCareSchema.DatabaseSchemaEntry.MONTHLY_PRESSURE_TABLE_NAME + " (" +
                AgedCareSchema.DatabaseSchemaEntry.MONTHLY_PRESSURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AgedCareSchema.DatabaseSchemaEntry.MONTHLY_PRESSURE_VALUE + " REAL, " +
                AgedCareSchema.DatabaseSchemaEntry.MONTHLY_CREATED_AT + " DEFAULT CURRENT_TIMESTAMP);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS ${PRESSURE_TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${GLUCOSE_TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${WEIGHT_TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${WEEKLY_PRESSURE_TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${MONTHLY_PRESSURE_TABLE_NAME}")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
        onUpgrade(db, oldVersion, newVersion)
    }

    fun addPressure(pressure: PressureModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_PRESSURE_DATE, pressure.date)
        contentValues.put(COLUMN_PRESSURE_TIME, pressure.time)
        contentValues.put(COLUMN_PRESSURE_SYSTOLIC, pressure.systolic)
        contentValues.put(COLUMN_PRESSURE_DIASTOLIC, pressure.diastolic)

        val success = db.insert(PRESSURE_TABLE_NAME, null, contentValues)
        db.close()
        return success

    }

    fun addGlucose(glucoseModel: GlucoseModel): Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_GLUCOSE_DATE, glucoseModel.date)
        values.put(COLUMN_GLUCOSE_TIME, glucoseModel.time)
        values.put(COLUMN_GLUCOSE_GLUCOSE, glucoseModel.glucose)
        var sucess = db.insert(GLUCOSE_TABLE_NAME, null, values)
        db.close()
        return sucess
    }


    fun addWeight(weightModel: WeightModel): Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_WEIGHT_DATE, weightModel.date)
        values.put(COLUMN_WEIGHT_TIME, weightModel.time)
        values.put(COLUMN_WEIGHT, weightModel.weight)
        var sucess = db.insert(WEIGHT_TABLE_NAME, null, values)
        db.close()
        return sucess
    }

    // data storage
    fun addWeeklyPressureData(dataModel: WeeklyDataModel): Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(WEEKLY_PRESSURE_VALUE, dataModel.value)
        var sucess = db.insert(WEEKLY_PRESSURE_TABLE_NAME, null, values)
        db.close()
        return sucess
    }

    fun addMonthlyPressureData(weightModel: MonthlyDataModel): Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(MONTHLY_PRESSURE_VALUE, weightModel.value)
        var sucess = db.insert(MONTHLY_PRESSURE_TABLE_NAME, null, values)
        db.close()
        return sucess
    }


    fun pressureXData(): ArrayList<Entry> {
        val xNewData = ArrayList<String>()
        val db = this.readableDatabase
        val query = db.rawQuery("SELECT * FROM pressure", null)
        val values: ArrayList<Entry> = ArrayList()


        while(query.moveToNext()){
            values.add(Entry(query.getFloat(query.getColumnIndex("diastolic")),
                query.getFloat(query.getColumnIndex("systolic"))))
            Collections.sort(values, EntryXComparator())

        }
        query.close()
        return values

    }


    fun glucoseXData(): ArrayList<Entry> {

        val db = this.readableDatabase
        val query = db.rawQuery("SELECT * FROM glucose", null)
        val values: ArrayList<Entry> = ArrayList()

        while(query.moveToNext()){
            values.add(Entry(query.getFloat(query.getColumnIndex("created_at")),
                query.getFloat(query.getColumnIndex("glucose"))))
            Collections.sort(values, EntryXComparator())

        }
        query.close()
        return values

    }

    fun weightXData(): ArrayList<Entry> {

        val db = this.readableDatabase
        val query = db.rawQuery("SELECT * FROM weight", null)
        val values: ArrayList<Entry> = ArrayList()

        while(query.moveToNext()){
            values.add(Entry(query.getFloat(query.getColumnIndex("created_at")),
                query.getFloat(query.getColumnIndex("weight"))))
            Collections.sort(values, EntryXComparator())

        }
        query.close()
        return values


    }


    // weekly query
    fun weeklyPressureQuery(){

        val db = this.readableDatabase
        // systolic_count
        val systolic = db.rawQuery("SELECT SUM(systolic) FROM pressure WHERE created_at  >= date('now','-7 days')", null)
        val sysotolic_count = db.rawQuery("SELECT COUNT(systolic) FROM pressure WHERE created_at  >= date('now','-7 days')", null)
        var systolic_total = systolic.count / sysotolic_count.count


        // diasotlic
        val diastolic = db.rawQuery("SELECT SUM(diastolic) FROM pressure WHERE created_at  >= date('now','-7 days')", null)
        val diastolic_count = db.rawQuery("SELECT COUNT(diastolic) FROM pressure WHERE created_at >= date('now', '-7 days')", null)
        val diastolic_total = diastolic.count / diastolic_count.count

        var store_data = systolic_total.toString() + "/" + diastolic_total.toString()

        // sqlite storage database
        addWeeklyPressureData(WeeklyDataModel(store_data))

    }
    // monthly query
    fun monthlyPressureQuery(){

        val db = this.readableDatabase
        val systolic = db.rawQuery("SELECT SUM(systolic) FROM pressure WHERE created_at < date('now','-30 days')", null)
        val systolic_count = db.rawQuery("SELECT COUNT(systolic) FROM pressure WHERE created_at < date('now','-30 days')", null)
        var systolic_total = systolic.count / systolic_count.count


        val diastolic = db.rawQuery("SELECT SUM(diastolic) FROM pressure WHERE created_at < date('now','-30 days')", null)
        val diastolic_count = db.rawQuery("SELECT COUNT(diastolic) FROM pressure WHERE created_at >= date('now', '-30 days')", null)
        var diastolic_total = diastolic.count / diastolic_count.count
        var store_data = systolic_total.toString() + "/" + diastolic_total.toString()


        // sqlite storage database
        addMonthlyPressureData(MonthlyDataModel(store_data))


    }



    companion object {
        const val DATABASE_NAME = "agedcare.db"
        const val DATABASE_VERSION = 5

        private val CREATE_PRESSURE_TABLE =
            "CREATE TABLE "+ AgedCareSchema.DatabaseSchemaEntry.PRESSURE_TABLE_NAME  + " (" +
                AgedCareSchema.DatabaseSchemaEntry.PRESSURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_PRESSURE_DATE + " TEXT," +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_PRESSURE_TIME + " TEXT," +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_PRESSURE_SYSTOLIC + " INTEGER," +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_PRESSURE_DIASTOLIC + " INTEGER," +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_PRESSURE_CREATED_AT + " DEFAULT CURRENT_TIMESTAMP);"


        private val CREATE_GLUCOSE_TABLE =
            "CREATE TABLE " + AgedCareSchema.DatabaseSchemaEntry.GLUCOSE_TABLE_NAME + " (" +
                    AgedCareSchema.DatabaseSchemaEntry.GLUCOSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_GLUCOSE_DATE + " TEXT," +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_GLUCOSE_TIME + " TEXT," +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_GLUCOSE_GLUCOSE + " REAL," +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_GLUCOSE_CREATED_AT + " DEFAULT CURRENT_TIMESTAMP);"



        private  val CREATE_WEIGHT_TABLE =
            "CREATE TABLE " + AgedCareSchema.DatabaseSchemaEntry.WEIGHT_TABLE_NAME + " (" +
                    AgedCareSchema.DatabaseSchemaEntry.WEIGHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_WEIGHT_DATE + " TEXT," +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_WEIGHT_TIME + " TEXT," +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_WEIGHT + " REAL," +
                    AgedCareSchema.DatabaseSchemaEntry.COLUMN_WEIGHT_CREATED_AT + " DEFAULT CURRENT_TIMESTAMP);"


    }
}