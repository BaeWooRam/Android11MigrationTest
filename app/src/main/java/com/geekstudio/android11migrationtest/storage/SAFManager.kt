package com.geekstudio.android11migrationtest.storage

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.*
import java.nio.channels.FileChannel
import android.widget.Toast
import android.content.ContentUris
import android.database.Cursor
import java.nio.charset.StandardCharsets
import android.content.ContentResolver
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Base64
import java.nio.charset.Charset


class SAFManager(private val activity: Activity) {
    private val debugTag = javaClass.simpleName

    /**
     *
     */
    fun createFile(mineType:String, fileName:String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = mineType
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
        activity.startActivityForResult(intent, REQUEST_CODE_CREATE_FILE)
    }

    /**
     *
     */
    fun openFile(mineType:String,) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = mineType
        }

        activity.startActivityForResult(intent, REQUEST_CODE_PICK_PDF_FILE)
    }


    /**
     *
     */
    fun dumpImageMetaData(uri: Uri) {
        val contentResolver = activity.contentResolver
        val cursor: Cursor? = contentResolver.query(
            uri, null, null, null, null, null)
        Log.i(debugTag, "dumpImageMetaData() cursor is null = ${cursor == null}")
        cursor?.use {
            Log.i(debugTag, "dumpImageMetaData() cursor count= ${it.count}")
            if (it.moveToFirst()) {
                val displayName: String = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                Log.i(debugTag, "dumpImageMetaData() Display Name: $displayName")

                val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)

                val size: String = if (!it.isNull(sizeIndex)) {
                    it.getString(sizeIndex)
                } else {
                    "Unknown"
                }
                Log.i(debugTag, "dumpImageMetaData() Size: $size")
            }
        }
    }

    @Throws(IOException::class)
    fun readTextFromUri(uri: Uri): String {
        val contentResolver = activity.contentResolver
        val stringBuilder = StringBuilder()
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
            }
        }
        return stringBuilder.toString()
    }

    fun alterDocument(uri: Uri) {
        try {
            val contentResolver = activity.contentResolver
            contentResolver.openFileDescriptor(uri, "w")?.use {
                FileOutputStream(it.fileDescriptor).use { fos ->
                    fos.write(
                        ("Overwritten at ${System.currentTimeMillis()}\n")
                            .toByteArray()
                    )
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object{
        const val REQUEST_CODE_CREATE_FILE = 1
        const val REQUEST_CODE_PICK_PDF_FILE = 2
    }
}