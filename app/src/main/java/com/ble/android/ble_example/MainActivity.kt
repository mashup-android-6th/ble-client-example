package com.ble.android.ble_example

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.le.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.system.Os.accept
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    ActivityCompat.requestPermissions(
      this, arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION
      ), 2000
    )

    val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//    if (bluetoothAdapter.isEnabled()) {
//      println("2323")
//      val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
//      val scanSettings = ScanSettings.Builder()
//        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//        .build()
//
//      val filters: List<ScanFilter> = ArrayList()
//
//      bluetoothLeScanner.startScan(filters, scanSettings, object : ScanCallback() {
//        override fun onScanFailed(errorCode: Int) {
//          println("232323" + errorCode)
//        }
//
//        override fun onScanResult(callbackType: Int, result: ScanResult?) {
//          val device: BluetoothDevice? = result?.device
//          Log.v("ff", "onScanResult: ${device?.address} name: ${device?.name} ")
//
//
//          if (device?.address == "50:77:05:E1:C4:97") {
//            println("323232323")
//          }
//        }
//
//        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
//        }
//      })
//    }

    val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice("50:77:05:E1:C4:97")

    ConnectThread(device).run()
  }
}

private class ConnectThread(private val mmDevice: BluetoothDevice) : Thread() {
  val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
  private val socket: BluetoothSocket?

  init {
    // Use a temporary object that is later assigned to mmSocket,
    // because mmSocket is final
    var tmp: BluetoothSocket? = null

    // Get a BluetoothSocket to connect with the given BluetoothDevice
    try {
      // MY_UUID is the app's UUID string, also used by the server code
      tmp = mmDevice.createRfcommSocketToServiceRecord(UUID.fromString("040cb2a6-d0f4-4854-953f-8cde192b23c0"))
    } catch (e: IOException) {
    }

    socket = tmp
  }

  override fun run() {
    // Cancel discovery because it will slow down the connection
    bluetoothAdapter.cancelDiscovery()

    try {
      // Connect the device through the socket. This will block
      // until it succeeds or throws an exception
      socket!!.connect()


    } catch (connectException: IOException) {
      // Unable to connect; close the socket and get out
      try {
        socket!!.close()
      } catch (closeException: IOException) {
      }

      return
    }

    println("123123123")
    val str: String = "power\n";
    socket.outputStream.write(str.toByteArray())

    // Do work to manage the connection (in a separate thread)
//    manageConnectedSocket(mmSocket)
  }

  /** Will cancel an in-progress connection, and close the socket  */
  fun cancel() {
    try {
      socket!!.close()
    } catch (e: IOException) {
    }
  }
}
