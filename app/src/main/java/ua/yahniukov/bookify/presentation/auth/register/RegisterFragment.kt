package ua.yahniukov.bookify.presentation.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.yahniukov.bookify.R
import ua.yahniukov.bookify.databinding.FragmentRegisterBinding
import ua.yahniukov.bookify.presentation.home.HomeActivity
import ua.yahniukov.bookify.utils.Result
import ua.yahniukov.bookify.utils.ValidateHelper
import ua.yahniukov.bookify.utils.hide
import ua.yahniukov.bookify.utils.navigate
import ua.yahniukov.bookify.utils.show
import ua.yahniukov.bookify.utils.showToast

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()
    private val validateHelper: ValidateHelper by lazy { ValidateHelper(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModel.uiState.observe(viewLifecycleOwner) { state ->
            handleUIState(state)
        }
        binding.toolbarRegister.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.buttonCreateAccount.setOnClickListener { register() }
    }

    private fun register() {
        val firstName = binding.editTextRegisterFirstName.text.toString()
        val lastName = binding.editTextRegisterLastName.text.toString()
        val email = binding.editTextRegisterEmail.text.toString()
        val password = binding.editTextRegisterPassword.text.toString()
        val confirmPassword = binding.editTextRegisterConfirmPassword.text.toString()
        if (
            validateHelper.validateFirstName(firstName) &&
            validateHelper.validateLastName(lastName) &&
            validateHelper.validateEmail(email) &&
            validateHelper.validatePassword(password) &&
            validateHelper.validateConfirmPassword(password, confirmPassword)
        ) {
            registerViewModel.register(firstName, lastName, email, password)
        }
    }

    private fun handleUIState(state: Result<Nothing>) {
        when (state) {
            is Result.Success -> {
                hideLoading()
                navigate(requireActivity(), HomeActivity::class.java)
            }

            is Result.Error -> {
                hideLoading()
                showToast(state.exception.message.toString())
            }

            Result.Loading -> {
                showLoading()
            }

            Result.Idle -> {}
        }
    }

    private fun showLoading() {
        binding.textNewAccount.hide()
        binding.progressBarRegister.show()
    }

    private fun hideLoading() {
        binding.progressBarRegister.hide()
        binding.textNewAccount.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}