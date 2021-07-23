package com.geekstudio.android11migrationtest.storage

import android.app.Activity
import android.content.Intent
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel


class StorageManager(private val activity: Activity) {
    private val debugTag = javaClass.simpleName

    fun createPublicRootFolder() {
        val dir = File("$APP_PUBLIC_ROOT_DIR_PATH")

        val isRoot = if (!dir.exists()) {
            dir.mkdir()
        } else
            true

        Log.d(debugTag, "createPublicRootFolder() isRoot = $isRoot")

        if (isRoot) {

        }
    }

    fun addPublicFolder(fileName: String) {
        val dir = File("$APP_PUBLIC_ROOT_DIR_PATH/$fileName")

        val isRoot = if (!dir.exists()) {
            dir.mkdir()
        } else
            true

        Log.d(debugTag, "addPublicFolder() isRoot = $isRoot")

        if (isRoot) {

        }
    }

    fun createRootFolder() {
        val dir = File("$APP_ROOT_DIR_PATH")

        val isRoot = if (!dir.exists()) {
            dir.mkdir()
        } else
            true

        Log.d(debugTag, "createRootFolder() isRoot = $isRoot")

        if (isRoot) {

        }
    }

    fun copyFileOrDirectory(srcDir: String, dstDir: String) {

        try {
            val src = File(srcDir)
            val dst = File(dstDir, src.name)

            if (src.isDirectory) {
                val files = src.listFiles()

                files.forEach { file ->
                    copyFile(file, dst)
                }
            } else {
                copyFile(src, dst)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun copyFile(sourceFile: File, destFile: File) {
        if (!destFile.parentFile.exists())
            destFile.parentFile.mkdirs()

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
        } finally {
            source?.close()
            destination?.close()
        }
    }

    fun addFolder(fileName: String) {
        val dir = File("$APP_ROOT_DIR_PATH/$fileName")

        val isRoot = if (!dir.exists()) {
            dir.mkdir()
        } else
            true

        Log.d(debugTag, "addFolder() isRoot = $isRoot")

        if (isRoot) {

        }
    }

    fun createFile(filename: String, mimeType: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = mimeType
            putExtra(Intent.EXTRA_TITLE, filename)
        }
        activity.startActivityForResult(intent, 1)
    }

    companion object {
        val APP_PUBLIC_ROOT_DIR_PATH =
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path}/Test"
        val APP_ROOT_DIR_PATH = "${Environment.getExternalStorageDirectory().path}/Test"
    }
}