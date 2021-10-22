package com.geekstudio.android11migrationtest.storage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.geekstudio.android11migrationtest.R
import com.geekstudio.android11migrationtest.contacts.ContactsManager
import com.geekstudio.android11migrationtest.databinding.ActivityStorageBinding
import com.geekstudio.android11migrationtest.permission.Permission
import com.geekstudio.android11migrationtest.permission.PermissionChecker
import java.io.File

class StorageActivity : AppCompatActivity() {
    private val debugTag = javaClass.simpleName
    private lateinit var storageManager: StorageManager
    private lateinit var safManager: SAFManager
    private lateinit var mPermissionChecker: PermissionChecker
    private lateinit var mPermissionListener: Permission.PermissionListener
    private lateinit var binding: ActivityStorageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initPermission()
        initStorageManager()
    }

    private fun initPermission() {
        mPermissionListener = getPermissionListener()
        mPermissionChecker = PermissionChecker().initPermissionLauncher(this@StorageActivity)

        mPermissionChecker
            .target(this@StorageActivity)
            .setPermissionListener(mPermissionListener)
            .request(getCheckPermission())
            .check()
    }

    private fun initStorageManager() {
        storageManager = StorageManager(this@StorageActivity)
        safManager = SAFManager(this@StorageActivity)

        binding.btCreateFolder.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                Toast.makeText(this@StorageActivity, "이벤트 시작", Toast.LENGTH_SHORT).show()
//                storageManager.saveTextQ(this@StorageActivity, "baewooramaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
//                storageManager.coverDownloadFolderFileQ("Download/OceanRoad/DB1", "hearoad.db","baewoorambaewoorambaewoorambaewoorambaewoorambaewoorambaewoorambaewoorambaewoorambaewoorambaewoorambaewooram")
//                storageManager.readFile()
//            }
            safManager.openFile("*/*")
        //            safManager.createFile("application/pdf","test.pdf")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(debugTag, "requestCode = $requestCode, resultCode = $resultCode, data = $data")

        when(requestCode){
            SAFManager.REQUEST_CODE_CREATE_FILE -> {
                if(Activity.RESULT_OK == resultCode && data != null){
                    data.data?.also { uri ->
                        safManager.dumpImageMetaData(uri)
                    }
                }
            }

            SAFManager.REQUEST_CODE_PICK_PDF_FILE -> {
                if(Activity.RESULT_OK == resultCode && data != null){
                    data.data?.also { uri ->
                        safManager.dumpImageMetaData(uri)
                    }
                }
            }
        }
    }

    private fun getPermissionListener(): Permission.PermissionListener {
        return object : Permission.PermissionListener {
            override fun onGrantedPermission() {

            }

            override fun onDenyPermission(denyPermissions: Array<String>) {
                Log.d(debugTag, "denyPermissions = $denyPermissions")
            }
        }
    }

    private fun getCheckPermission(): Array<String> {
        return arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        )
    }
}