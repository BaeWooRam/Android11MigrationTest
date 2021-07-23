package com.geekstudio.android11migrationtest.storage

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.geekstudio.android11migrationtest.R
import com.geekstudio.android11migrationtest.contacts.ContactsManager
import com.geekstudio.android11migrationtest.permission.Permission
import com.geekstudio.android11migrationtest.permission.PermissionChecker
import java.io.File

class StorageActivity : AppCompatActivity() {
    private val debugTag = javaClass.simpleName
    private lateinit var storageManager: StorageManager
    private lateinit var mPermissionChecker: PermissionChecker
    private lateinit var mPermissionListener: Permission.PermissionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPermission()
        initStorageManager()
    }

    private fun initPermission(){
        mPermissionListener = getPermissionListener()
        mPermissionChecker = PermissionChecker().initPermissionLauncher(this@StorageActivity)

        mPermissionChecker
            .target(this@StorageActivity)
            .setPermissionListener(mPermissionListener)
            .request(getCheckPermission())
            .check()
    }

    private fun initStorageManager(){
        storageManager = StorageManager(this@StorageActivity)
        storageManager.createRootFolder()
        storageManager.createPublicRootFolder()
        storageManager.addFolder("wooram")
        storageManager.addPublicFolder("wooram")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(debugTag, "requestCode = $requestCode, resultCode = $resultCode, data = $data")
    }

    private fun getPermissionListener(): Permission.PermissionListener{
        return object :Permission.PermissionListener{
            override fun onGrantedPermission() {

            }

            override fun onDenyPermission(denyPermissions: Array<String>) {
                Log.d(debugTag, "denyPermissions = $denyPermissions")
            }
        }
    }

    private fun getCheckPermission(): Array<String>{
        return arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}