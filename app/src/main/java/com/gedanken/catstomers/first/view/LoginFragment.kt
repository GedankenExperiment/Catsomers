package com.gedanken.catstomers.first.view

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gedanken.catstomers.R
import com.gedanken.catstomers.first.model.remote.LogInStatus
import com.gedanken.catstomers.first.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {
    private lateinit var circularProgressDrawable: ProgressBar
    var isPasswordShowing: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.loggedIn.observe(viewLifecycleOwner, Observer { onLoginStatus(it) })
        loginViewModel.errorMessage.observe(viewLifecycleOwner, Observer { onError(it) })
        circularProgressDrawable = view.findViewById<ProgressBar>(R.id.progress)
        view.findViewById<Button>(R.id.button_login).setOnClickListener {
            val emailWritten: String = email_ti_et.text.toString()
            val passwordWritten: String = password_ti_et.text.toString()
            val correctValidation = validate(emailWritten, passwordWritten)
            val mocking : Boolean = true
            if(mocking){
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }else{
                if(correctValidation){
                    circularProgressDrawable.visibility = View.VISIBLE
                    loginViewModel.logIn(emailWritten, passwordWritten)
                }else{
                    Toast.makeText(activity, getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show()
                }
            }

        }
        view.findViewById<Button>(R.id.show_hide_password).setOnClickListener {
            if(isPasswordShowing){
                password_ti_et.transformationMethod = HideReturnsTransformationMethod.getInstance()
                view.findViewById<Button>(R.id.show_hide_password).text = getString(R.string.show_password)
            }else {
                password_ti_et.transformationMethod = PasswordTransformationMethod.getInstance()
                view.findViewById<Button>(R.id.show_hide_password).text = getString(R.string.hide_password)
            }
            isPasswordShowing = !isPasswordShowing
        }
    }
    /**
     * Regex validation to do
     * @param logInStatus containing a boolean 'isLogged'
     */
    private fun validate(userName: String, password: String): Boolean {
        return userName == getString(R.string.mocked_email) && password == getString(R.string.mocked_password);
    }

    /**
     *
     * @param logInStatus containing a boolean 'isLogged'
     */
    /**
     * Method called when a change in MainViewModel.logInStatus MutableLiveData is observed
     */
    private fun onLoginStatus(logInStatus: LogInStatus) {
        circularProgressDrawable.visibility = View.INVISIBLE
        if(logInStatus.isLogged){
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            Toast.makeText(activity, getString(R.string.logged_in), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, getString(R.string.wrong_credential), Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * Method called when a change in MainViewModel.errorMessage MutableLiveData is observed
     * @param error Error message describing what went wrong
     */
    private fun onError(error: String) {
        circularProgressDrawable.visibility = View.INVISIBLE
        // a simple toast in case things went wrong
        error.let {
            if (!it.isBlank()) {
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}