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
import android.util.Base64
import java.nio.charset.Charset


class StorageManager(private val activity: Activity) {
    private val debugTag = javaClass.simpleName

    fun openFile(path: String) {
        val dir = File(path)
        Log.d(debugTag, "file name = ${dir.name}")

        if (!dir.exists()) {
            Log.d(debugTag, "file not exists")
        } else
            Log.d(debugTag, "parentFile = ${dir.parentFile}, listFile = ${dir.listFiles()}")
    }

    fun readFile(path: String) {
        val file = File(path)
        val fileReader = FileReader(file)
        val bufferedReader = BufferedReader(fileReader)

        bufferedReader.readLines().forEach() {
            Log.d("Test", it)
        }
    }

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

    fun createRootFolder() {
        val dir = File("$APP_ROOT_DIR_PATH")

        val isRoot = if (!dir.exists()) {
            dir.mkdir()
        } else
            true

        Log.d(debugTag, "createRootFolder() isRoot = $isRoot")

        if (isRoot) {

        } else {

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
        Log.d(
            javaClass.simpleName,
            "copyFile() sourceFile exists = ${sourceFile.exists()}, destFile exists = ${destFile.exists()}"
        )
        if (!destFile.parentFile.exists()) {
            Log.d(javaClass.simpleName, "destFile.parentFile.mkdirs()")
            destFile.parentFile.mkdirs()
        }

        if (!destFile.exists()) {
            Log.d(javaClass.simpleName, "destFile.createNewFile()")
            destFile.createNewFile()
        }

        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
        } catch (e: Exception) {
            Log.d(javaClass.simpleName, "Exception msg = ${e.message}")
            e.printStackTrace()
        } finally {
            source?.close()
            destination?.close()
        }
    }

    fun getCreateFileIntent(): Intent {
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "invoice.pdf")
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

    fun addFolderAt31(context: Context) {
        // 경로와 이미지 정보 명시
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "minion")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        // 파일이 생성될 저장소 명시
        val imageUri =
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        try {
            // assets으로 부터 파일을 읽고 위에서 생성한 파일 쓰기
            val inputStream: InputStream = context.assets.open("minion.jpg")
            var bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

            context.contentResolver.openOutputStream(imageUri!!).use { out ->
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(imageUri!!, values, null, null)
        } catch (e: Exception) {
            Log.d(debugTag, "addFolderAt31() Bitmap Error : ${e.message}")
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun addTextAt31(context: Context) {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "sdFile.db")
            put(MediaStore.MediaColumns.MIME_TYPE, "plain/text")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        var input = resolver.openInputStream(uri!!)
        var dis = DataInputStream(input)
        var value1 = dis.readInt()
        var value2 = dis.readDouble()
        var value3 = dis.readUTF()
        dis.close()
    }

    // filename can be a String for a new file, or an Uri to append it
    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveTextQ(
        context: Context,
        text: String
    ) {
        val values = ContentValues()
        values.put(MediaStore.DownloadColumns.DISPLAY_NAME, "haeroad11.db")
        values.put(MediaStore.DownloadColumns.MIME_TYPE, "text/*")
        values.put(MediaStore.DownloadColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/OceanRoad/DB1")

        val fileUri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values) ?: throw Error("fileUri is null!")
        val outputStream = context.contentResolver.openOutputStream(fileUri, "wa") ?: throw Error("fileUri is null!")
        outputStream.write(text.toByteArray(charset("UTF-8")))
        outputStream.close()
    }

    /**
     *
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun coverDownloadFolderFileQ(relativePath:String, displayName: String, content: String) {
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_TAKEN,
            MediaStore.Files.FileColumns.MIME_TYPE
        )

        /*val selection = MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE ?"
        val selectionArgs = arrayOf(displayName)*/
        val cursor: Cursor? =
            activity.contentResolver.query(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), projection, null, null, null)

        var uri: Uri? = null
        Toast.makeText(activity, "cursor count = ${cursor?.count}", Toast.LENGTH_LONG).show()
        if (cursor?.count == 0) {
            return
        } else {
            //해당 파일 찾기
            while (cursor?.moveToNext() == true) {
                val fileName = cursor?.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME))
                val mineType = cursor?.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE))
                Log.d(javaClass.simpleName, "FileName = $fileName, mineType = $mineType")

                if (fileName == displayName) {
                    Log.d(javaClass.simpleName, "File find!")
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
                    uri = ContentUris.withAppendedId(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), id)
                    break
                }
            }

            //파일 덮어씌기
            if (uri == null) {
                return
            } else {
                try {
                    Log.d(javaClass.simpleName, "File written start")
                    val outputStream: OutputStream? = activity.contentResolver.openOutputStream(uri, "rwt")
                    outputStream?.write(content.toByteArray())
                    outputStream?.close()
                    Log.d(javaClass.simpleName, "File written successfully")
                } catch (e: IOException) {
                    Log.d(javaClass.simpleName, "Fail to write file")
                }
            }
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


    /**
     *
     */
    fun writeFile(context: Context, fileInfo: StorageFileInfo): Result<Boolean> {
        return kotlin.runCatching {
            val values = ContentValues()
            values.put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                fileInfo.displayName
            ) //file name
            values.put(
                MediaStore.MediaColumns.MIME_TYPE,
                fileInfo.mineType
            ) //file extension, will automatically add to file
            values.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                fileInfo.relativePath
            ) //end "/" is not mandatory
            val uri: Uri? = context.contentResolver.insert(
                MediaStore.Files.getContentUri("external"),
                values
            ) //important!

            uri?.run {
                val outputStream: OutputStream? = context.contentResolver.openOutputStream(uri)
                outputStream?.write(fileInfo.content.toByteArray())
                outputStream?.close()
                return@runCatching true
            }

            return@runCatching false
        }
    }

    /**
     *
     */
    fun findFileContent(context: Context, fileInfo: StorageFileInfo):String?{
        val contentUri = MediaStore.Files.getContentUri("external")

        val selection = MediaStore.DownloadColumns.RELATIVE_PATH + "=?"

        val selectionArgs = arrayOf(fileInfo.relativePath)

        val cursor: Cursor? =
            context.contentResolver.query(contentUri, null, selection, selectionArgs, null)

        var uri: Uri? = null

        return if (cursor?.count == 0) {
            Toast.makeText(context, "findFileContent() ${fileInfo.relativePath}/${fileInfo.displayName} not found", Toast.LENGTH_SHORT).show()
            null
        } else {
            while (cursor?.moveToNext() == true) {
                val fileName: String? =
                    cursor?.getString(cursor.getColumnIndex(MediaStore.DownloadColumns.DISPLAY_NAME))
                if (fileName == fileInfo.displayName) {
                    val id: Long =
                        cursor?.getLong(cursor.getColumnIndex(MediaStore.DownloadColumns._ID))
                    uri = ContentUris.withAppendedId(contentUri, id)
                    break
                }
            }

            if (uri == null) {
                Toast.makeText(context, "findFileContent() ${fileInfo.relativePath}/${fileInfo.displayName} not found", Toast.LENGTH_SHORT).show()
                null
            } else {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val size = inputStream?.available()
                val bytes = ByteArray(size ?: 0 )
                inputStream?.read(bytes)
                inputStream?.close()
                String(bytes, StandardCharsets.UTF_8)
            }
        }
    }

    /**
     *
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun coverFile(context: Context, fileInfo: StorageFileInfo) {
        val contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI

        val selection = MediaStore.DownloadColumns.RELATIVE_PATH + "=?"

        val selectionArgs = arrayOf(fileInfo.relativePath) //must include "/" in front and end

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_TAKEN
        )

        val cursor: Cursor? =
            context.contentResolver.query(contentUri, projection, selection, selectionArgs, null)

        var uri: Uri? = null

        if (cursor?.count == 0) {
            Toast.makeText(context, "coverFile() ${fileInfo.relativePath}/${fileInfo.displayName} not found", Toast.LENGTH_LONG).show()
        } else {
            //해당 파일 찾기
            while (cursor?.moveToNext() == true) {
                val fileName =
                    cursor?.getString(cursor.getColumnIndex(MediaStore.DownloadColumns.DISPLAY_NAME))
                if (fileName == fileInfo.displayName) {                          //must include extension
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.DownloadColumns._ID))
                    uri = ContentUris.withAppendedId(contentUri, id)
                    break
                }
            }

            //파일 덮어씌기
            if (uri == null) {
                Toast.makeText(context, "coverFile() ${fileInfo.relativePath}/${fileInfo.displayName} not found", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val outputStream: OutputStream? = context.contentResolver.openOutputStream(
                        uri,
                        "rwt"
                    ) //overwrite mode, see below
                    outputStream?.write(fileInfo.content.toByteArray())
                    outputStream?.close()
                    Toast.makeText(context, "File written successfully", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    Toast.makeText(context, "Fail to write file", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun readFile(): String? {
        var data: String
        val outputFilename = "haeroad.db"
        val outputDirectory = "OceanRoad/DB1/"
        val resolver: ContentResolver = activity.contentResolver
        val values = ContentValues()

        values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, outputFilename)
        values.put(MediaStore.Files.FileColumns.MIME_TYPE, "text/*")
        values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/" + outputDirectory)
        val uri = resolver.insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), values) ?: return null

        try {
            val inputSteam = resolver.openInputStream(uri!!)
            val size = inputSteam!!.available()
            val buffer = ByteArray(size)
            inputSteam.read(buffer)
            inputSteam.close()
            val text = String(buffer, Charset.forName("UTF-8"))
            data = String(Base64.decode(text, Base64.DEFAULT))
            Log.d(javaClass.simpleName, "readFile: decoded = $data")
        } catch (e: FileNotFoundException) {
            data = ""
            e.printStackTrace()
        } catch (e: IOException) {
            data = ""
            e.printStackTrace()
        }
        return data
    }

    /**
     *
     */
    fun checkFolder(file: File):FolderState {
        if (!file.parentFile.exists()) {
            Log.d(javaClass.simpleName, "checkFolder() ParentFile is Create Dirs")
            return FolderState.PARENT_FILE_NOT_EXISTS
        }

        if (!file.exists()) {
            Log.d(javaClass.simpleName, "checkFolder() File is Create NewFile")
            return FolderState.TARGET_FILE_NOT_EXISTS
        }

        return FolderState.OK
    }

    /**
     *
     */
    enum class FolderState{
        PARENT_FILE_NOT_EXISTS,
        TARGET_FILE_NOT_EXISTS,
        OK
    }
    data class StorageFileInfo(
        val displayName: String,
        val mineType: String,
        val relativePath: String,
        val content: String
    )

    companion object {
        val APP_PUBLIC_ROOT_DIR_PATH =
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path}/Test"
        val APP_ROOT_DIR_PATH = "${Environment.getExternalStorageDirectory().path}/Test"
    }
}