package com.knightshell.agedcare.model

import android.provider.BaseColumns

object AgedCareSchema {

    class DatabaseSchemaEntry: BaseColumns {
        companion object {

            // PRESSURE TABLE
            val PRESSURE_TABLE_NAME = "pressure"
            val PRESSURE_ID = "id"
            val COLUMN_PRESSURE_DATE = "date"
            val COLUMN_PRESSURE_TIME = "time"
            val COLUMN_PRESSURE_SYSTOLIC ="systolic"
            val COLUMN_PRESSURE_DIASTOLIC = "diastolic"
            val COLUMN_PRESSURE_CREATED_AT = "created_at"

            // GLUCOSE TABLE
            val GLUCOSE_TABLE_NAME = "glucose"
            val GLUCOSE_ID = "id"
            val COLUMN_GLUCOSE_DATE = "date"
            val COLUMN_GLUCOSE_TIME = "time"
            val COLUMN_GLUCOSE_GLUCOSE = "glucose"
            val COLUMN_GLUCOSE_CREATED_AT = "created_at"

            // WEIGHT TABLE
            val WEIGHT_TABLE_NAME = "weight"
            val WEIGHT_ID = "id"
            val COLUMN_WEIGHT_DATE = "date"
            val COLUMN_WEIGHT_TIME = "time"
            val COLUMN_WEIGHT = "weight"
            val COLUMN_WEIGHT_CREATED_AT = "created_at"

            // WEEKLY TABLE
            val WEEKLY_PRESSURE_TABLE_NAME = "weekly_pressure"
            val WEEKLY_PRESSURE_ID = "id"
            val WEEKLY_PRESSURE_VALUE = "value"
            val WEEKLY_CREATED_AT = "created_at"


            // MONTHLY TABLE
            val MONTHLY_PRESSURE_TABLE_NAME = "monthly_pressure"
            val MONTHLY_PRESSURE_ID = "id"
            val MONTHLY_PRESSURE_VALUE = "value"
            val MONTHLY_CREATED_AT = "created_at"

        }
    }
}