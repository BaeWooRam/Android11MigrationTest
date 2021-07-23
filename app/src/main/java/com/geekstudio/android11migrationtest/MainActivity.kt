package com.geekstudio.android11migrationtest

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.geekstudio.android11migrationtest.contacts.ContactsManager
import com.geekstudio.android11migrationtest.permission.Permission
import com.geekstudio.android11migrationtest.permission.PermissionChecker

class MainActivity : AppCompatActivity() {
    private lateinit var mPermissionChecker: PermissionChecker
    private lateinit var mPermissionListener: Permission.PermissionListener
    private val debugTag = javaClass.simpleName

    private lateinit var contactsManager: ContactsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPermission()
        initContacts()
    }

    private fun initPermission(){
        mPermissionListener = getPermissionListener()
        mPermissionChecker = PermissionChecker().initPermissionLauncher(this@MainActivity)

        mPermissionChecker
            .target(this@MainActivity)
            .setPermissionListener(mPermissionListener)
            .request(getCheckPermission())
            .check()
    }

    private fun initContacts(){
        contactsManager = ContactsManager(this@MainActivity)

        findViewById<TextView>(R.id.tvContacts).run {
            text = contactsManager.getContacts().toString()
        }
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