package com.geekstudio.android11migrationtest

import android.util.JsonWriter
import android.util.Log
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private var isWarningLeisure: String = "N"
    private var isWarningVTS: String = "N"
    private var isWarningBreakWater: String = "N"

    @Test
    fun `모두_N일때_테스트`() {
        isWarningLeisure = "N"
        isWarningVTS = "N"
        isWarningBreakWater = "N"

        assertTrue(getTargetZone(getSampleData())!!["NAME"].toString() == "TEST1")
    }


    @Test
    fun `isWarningVTS_Y일때_테스트`() {
        isWarningLeisure = "N"
        isWarningVTS = "Y"
        isWarningBreakWater = "N"

        assertTrue(getTargetZone(getSampleData())!!["NAME"].toString() == "TEST1")
    }

    @Test
    fun `isWarningLeisure_Y일때_테스트`() {
        isWarningLeisure = "Y"
        isWarningVTS = "N"
        isWarningBreakWater = "N"

        assertTrue(getTargetZone(getSampleData())!!["NAME"].toString() == "TEST2")
    }

    @Test
    fun `isWarningBreakWater_Y일때_테스트`() {
        isWarningLeisure = "N"
        isWarningVTS = "N"
        isWarningBreakWater = "Y"

        assertTrue(getTargetZone(getSampleData())!!["NAME"].toString() == "TEST3")
    }

    @Test
    fun `모두_Y일때_테스트`() {
        isWarningLeisure = "Y"
        isWarningVTS = "Y"
        isWarningBreakWater = "Y"

        assertTrue(getTargetZone(getSampleData())!!["NAME"].toString() == "TEST1")
    }

    @Test
    fun `isWarningVTS_isWarningBreakWater_Y일때_테스트`() {
        isWarningLeisure = "N"
        isWarningVTS = "Y"
        isWarningBreakWater = "Y"

        assertTrue(getTargetZone(getSampleData())!!["NAME"].toString() == "TEST1")
    }

    @Test
    fun `isWarningLeisure_isWarningBreakWater_Y일때_테스트`() {
        isWarningLeisure = "Y"
        isWarningVTS = "N"
        isWarningBreakWater = "Y"

        assertTrue(getTargetZone(getSampleData())!!["NAME"].toString() == "TEST2")
    }

    @Test
    fun `isWarningVTS_isWarningLeisure_Y일때_테스트`() {
        isWarningLeisure = "Y"
        isWarningVTS = "Y"
        isWarningBreakWater = "N"

        assertTrue(getTargetZone(getSampleData())!!["NAME"].toString() == "TEST1")
    }

    private fun getSampleData():JSONArray = JSONParser().parse("[{\"NAME\":\"TEST1\",\"MAP_TYPE\":\"A\"}," +
            "{\"NAME\":\"TEST2\",\"MAP_TYPE\":\"B\"}," +
            "{\"NAME\":\"TEST3\",\"MAP_TYPE\":\"D\"}," +
            "{\"NAME\":\"TEST4\",\"MAP_TYPE\":\"B\"}," +
            "{\"NAME\":\"TEST5\",\"MAP_TYPE\":\"A\"}," +
            "{\"NAME\":\"TEST6\",\"MAP_TYPE\":\"D\"}]") as JSONArray

    private fun getTargetZone(nearZoneJson: JSONArray): JSONObject? {
        for (i in 0 until nearZoneJson.size) {
            val target: JSONObject = nearZoneJson[i] as JSONObject
            println("getTargetZone() target = $target")

            val mapType = if (target["MAP_TYPE"] == null) "" else target["MAP_TYPE"].toString()
            println("getTargetZone() mapType = $mapType")

            if (isWarningVTS == "Y" && mapType == "A") {
                println("getTargetZone() WarningVTS")
                return target
            } else if (isWarningLeisure == "Y" && mapType == "B") {
                println("getTargetZone() WarningLeisure")
                return target
            } else if (isWarningBreakWater == "Y" && mapType == "D") {
                println("getTargetZone() WarningBreakWater")
                return target
            }
        }
        return nearZoneJson[0] as JSONObject
    }
}