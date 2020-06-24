package com.mocan.autoreflex.ui.signup

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mocan.autoreflex.R
import com.mocan.autoreflex.ui.login.LoginViewModel
import com.mocan.autoreflex.ui.login.LoginViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [IDFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [IDFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IDFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private val READ_REQUEST_CODE: Int = 42
    private val REQUEST_IMAGE_CAPTURE: Int = 1
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mStorageRef: StorageReference
    private lateinit var functions: FirebaseFunctions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        loginViewModel = ViewModelProviders.of(this,
            LoginViewModelFactory()
        )
            .get(LoginViewModel::class.java)
        mStorageRef = FirebaseStorage.getInstance().reference


        checkFilePermissions()
        functions = FirebaseFunctions.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_id, container, false)
        val searchBtn = root.findViewById<Button>(R.id.button3)
        val takePhoto = root.findViewById<Button>(R.id.button2)

        root.findViewById<Button>(R.id.tmpFinish).setOnClickListener { _ ->  onButtonPressed(Uri.EMPTY)}

        searchBtn.setOnClickListener { performFileSearch() }
        takePhoto.setOnClickListener { perforTakePhoto() }

        return root
    }

    private fun performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            addCategory(Intent.CATEGORY_OPENABLE)

            // Filter to show only images, using the image MIME data type.
            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
            // To search for all documents available via installed storage providers,
            // it would be "*/*".
            type = "image/*"
        }
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    private fun perforTakePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            this.requireContext().contentResolver.openInputStream(resultData?.data!!).use {
                stream: InputStream? -> uploadFile(stream!!.readBytes())
            }
//            uploadFile(resultData?.dataString!!.toByteArray())

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = resultData?.extras?.get("data") as Bitmap
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            uploadFileMemory(data)
        }
    }


    private fun uploadFileMemory(data: ByteArray) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Get new Instance ID token
                        val userID = task.result?.token + " MYUID " + user.uid
                        val storageReference = mStorageRef.child("new_users/$userID.jpg")
                        storageReference.putBytes(encrypt(data)).addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                Log.e("file uploaded", "Success!")
                            }
                        }
                    }
                })
        }
    }

    private fun generateSecretKey(): SecretKey? {
        val secureRandom = SecureRandom()
        val keyGenerator = KeyGenerator.getInstance("AES")
        //generate a key with secure random
        keyGenerator?.init(128, secureRandom)
        return keyGenerator?.generateKey()
    }

    private fun saveSecretKey(secretKey: SecretKey) {
        val secretPlain = Base64.encodeToString(secretKey.encoded, Base64.NO_WRAP)
        Log.i("key", secretPlain)
        Log.i("key", secretPlain.length.toString())
        Log.i("org_key", secretKey.toString())
        Log.i("org_key", secretKey.encoded.toString())
        Log.i("org_key", secretKey.encoded.size.toString())


        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val data = hashMapOf(
            "password" to secretPlain,
            "uid" to uid
        )

        Log.e("uid", uid)

        functions
            .getHttpsCallable("encrypt")
            .call(data)
            .addOnFailureListener {
            Log.wtf("FF", it)
        }
            .addOnSuccessListener {
                Log.i("working", "yeah")
            }

    }

    private fun encrypt(yourKey: SecretKey, fileData: ByteArray): ByteArray {
        val data = yourKey.encoded
        Log.e("size", data.size.toString())
        val skeySpec = SecretKeySpec(data, "AES")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        val iv = ByteArray(12)
        SecureRandom().nextBytes(iv)

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, GCMParameterSpec(128, iv))
        val cipherText = cipher.doFinal(fileData)

        val byteBuffer: ByteBuffer = ByteBuffer.allocate(iv.size + cipherText.size)
        byteBuffer.put(iv)
        byteBuffer.put(cipherText)
        return byteBuffer.array()
    }


    private fun encrypt(data: ByteArray): ByteArray {
        val secretKey = generateSecretKey()
        saveSecretKey(secretKey!!)
        return encrypt(secretKey, data)
    }

    private fun uploadFile(uri: ByteArray) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {

            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Get new Instance ID token
                        val userID = task.result?.token + " MYUID " + user.uid
                        val storageReference = mStorageRef.child("new_users/$userID.jpg")
                        storageReference.putBytes(encrypt(uri)).addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                Log.e("file uploaded", "Success!")
                            }
                        }
                    }
                })
        }

    }

    private fun checkFilePermissions() {
        var permissionCheck = checkSelfPermission(requireContext(), "Manifest.permission.READ_EXTERNAL_STORAGE")
        permissionCheck += checkSelfPermission(requireContext(), "Manifest.permission.WRITE_EXTERNAL_STORAGE")
        if (permissionCheck != 0) {
            this.requestPermissions(
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1001
            ) //Any number
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IDFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IDFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
