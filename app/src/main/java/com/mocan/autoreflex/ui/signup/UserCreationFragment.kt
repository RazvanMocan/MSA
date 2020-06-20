package com.mocan.autoreflex.ui.signup

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.mocan.autoreflex.R
import com.mocan.autoreflex.ui.login.LoginViewModel
import com.mocan.autoreflex.ui.login.LoginViewModelFactory
import android.app.Activity
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat.getSystemService
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.mocan.autoreflex.ui.login.afterTextChanged


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [UserCreationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [UserCreationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserCreationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var username: EditText
    private lateinit var password: EditText
//    private lateinit var button: Button

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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_user_creation, container, false)
        val username = root.findViewById<EditText>(R.id.username_create)
        val password = root.findViewById<EditText>(R.id.password_create)
        val loading = root.findViewById<ProgressBar>(R.id.create_loading)
        val signUp = root.findViewById<Button>(R.id.button)

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply{
            afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }}

        loginViewModel.loginFormState.observe(this@UserCreationFragment, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            signUp.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        signUp.setOnClickListener {
            loading.visibility = View.VISIBLE

            password.onEditorAction(EditorInfo.IME_ACTION_DONE)

            val task = loginViewModel.createUser(username.text.toString(), password.text.toString())
            task.addOnCompleteListener { task1 ->
                loading.visibility = View.INVISIBLE
                if (task1.isSuccessful) {
                    onButtonPressed(1)
                }
                Toast.makeText(root.context, "User: " + loginViewModel.alreadyLogged().toString(),
                    Toast.LENGTH_LONG).show()
            }
        }
        return root
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Int) {
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
        fun onFragmentInteraction(int: Int)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserCreationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserCreationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
