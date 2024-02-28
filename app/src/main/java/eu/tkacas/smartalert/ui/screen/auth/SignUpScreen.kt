package eu.tkacas.smartalert.ui.screen.auth

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import eu.tkacas.smartalert.R
import eu.tkacas.smartalert.ui.component.HeadingTextComponent
import eu.tkacas.smartalert.R.string.create_account
import eu.tkacas.smartalert.R.string.firstname
import eu.tkacas.smartalert.R.string.lastname
import eu.tkacas.smartalert.R.string.email
import eu.tkacas.smartalert.R.string.password
import eu.tkacas.smartalert.R.string.terms_and_conditions
import eu.tkacas.smartalert.R.string.register
import eu.tkacas.smartalert.ui.component.ButtonComponent
import eu.tkacas.smartalert.ui.component.ButtonLandscapeComponent
import eu.tkacas.smartalert.ui.component.CheckboxComponent
import eu.tkacas.smartalert.ui.component.ClickableLoginTextComponent
import eu.tkacas.smartalert.ui.component.DividerTextComponent
import eu.tkacas.smartalert.ui.component.HeadingTextLandscapeComponent
import eu.tkacas.smartalert.ui.component.TextFieldComponent
import eu.tkacas.smartalert.ui.component.PasswordTextFieldComponent
import eu.tkacas.smartalert.ui.component.PasswordTextFieldLandscapeComponent
import eu.tkacas.smartalert.ui.component.TextFieldLandscapeComponent
import eu.tkacas.smartalert.ui.event.SignupUIEvent
import eu.tkacas.smartalert.ui.theme.SkyBlue
import eu.tkacas.smartalert.viewmodel.auth.SignupViewModel


@Composable
fun SignUpScreen(navController: NavController? = null) {

    val signupViewModel: SignupViewModel = viewModel()
    signupViewModel.navController = navController

    val context = LocalContext.current

    val config = LocalConfiguration.current

    val portraitMode = remember { mutableStateOf(config.orientation ) }

    if (portraitMode.value == Configuration.ORIENTATION_PORTRAIT) {
        //PortraitLayout()
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(28.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    HeadingTextComponent(value = stringResource(id = create_account))
                    Spacer(modifier = Modifier.height(20.dp))
                    TextFieldComponent(
                        labelValue = stringResource(id = firstname),
                        painterResource(id = R.drawable.profile),
                        onTextChanged = {
                            signupViewModel.onEvent(SignupUIEvent.FirstNameChanged(it))
                        },
                        errorStatus = signupViewModel.registrationUIState.value.firstNameError
                    )
                    TextFieldComponent(
                        labelValue = stringResource(id = lastname),
                        painterResource(id = R.drawable.profile),
                        onTextChanged = {
                            signupViewModel.onEvent(SignupUIEvent.LastNameChanged(it))
                        },
                        errorStatus = signupViewModel.registrationUIState.value.lastNameError
                    )
                    TextFieldComponent(
                        labelValue = stringResource(id = email),
                        painterResource(id = R.drawable.email),
                        onTextChanged = {
                            signupViewModel.onEvent(SignupUIEvent.EmailChanged(it))
                        },
                        errorStatus = signupViewModel.registrationUIState.value.emailError
                    )
                    PasswordTextFieldComponent(
                        labelValue = stringResource(id = password),
                        painterResource(id = R.drawable.password),
                        onTextSelected = {
                            signupViewModel.onEvent(SignupUIEvent.PasswordChanged(it))
                        },
                        errorStatus = signupViewModel.registrationUIState.value.passwordError
                    )
                    CheckboxComponent(
                        value = stringResource(id = terms_and_conditions),
                        onTextSelected = {
                            navController?.navigate("termsAndConditionsScreen")
                        },
                        onCheckedChange = {
                            signupViewModel.onEvent(SignupUIEvent.PrivacyPolicyCheckBoxClicked(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    ButtonComponent(
                        value = stringResource(id = register),
                        onButtonClicked = {
                            signupViewModel.onEvent(SignupUIEvent.RegisterButtonClicked)
                        },
                        isEnabled = signupViewModel.allValidationsPassed.value
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    DividerTextComponent()

                    ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {
                        navController?.navigate("login") }, context = context)
                }


            }
            if (signupViewModel.signUpInProgress.value) {
                CircularProgressIndicator(color = SkyBlue)
            }
        }
    } else {
        //LandscapeLayout()
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(28.dp)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.weight(1f)) {
                            HeadingTextLandscapeComponent(value = stringResource(id = create_account))
                            Spacer(modifier = Modifier.height(20.dp))
                            TextFieldLandscapeComponent(
                                labelValue = stringResource(id = firstname),
                                painterResource(id = R.drawable.profile),
                                onTextChanged = {
                                    signupViewModel.onEvent(SignupUIEvent.FirstNameChanged(it))
                                },
                                errorStatus = signupViewModel.registrationUIState.value.firstNameError
                            )
                            TextFieldLandscapeComponent(
                                labelValue = stringResource(id = email),
                                painterResource(id = R.drawable.email),
                                onTextChanged = {
                                    signupViewModel.onEvent(SignupUIEvent.EmailChanged(it))
                                },
                                errorStatus = signupViewModel.registrationUIState.value.emailError
                            )
                            CheckboxComponent(
                                value = stringResource(id = terms_and_conditions),
                                onTextSelected = {
                                    navController?.navigate("termsAndConditionsScreen")
                                },
                                onCheckedChange = {
                                    signupViewModel.onEvent(
                                        SignupUIEvent.PrivacyPolicyCheckBoxClicked(
                                            it
                                        )
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Spacer(modifier = Modifier.height(55.dp))

                            TextFieldLandscapeComponent(
                                labelValue = stringResource(id = lastname),
                                painterResource(id = R.drawable.profile),
                                onTextChanged = {
                                    signupViewModel.onEvent(SignupUIEvent.LastNameChanged(it))
                                },
                                errorStatus = signupViewModel.registrationUIState.value.lastNameError
                            )
                            PasswordTextFieldLandscapeComponent(
                                labelValue = stringResource(id = password),
                                painterResource(id = R.drawable.password),
                                onTextSelected = {
                                    signupViewModel.onEvent(SignupUIEvent.PasswordChanged(it))
                                },
                                errorStatus = signupViewModel.registrationUIState.value.passwordError
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                            ButtonLandscapeComponent(
                                value = stringResource(id = register),
                                onButtonClicked = {
                                    signupViewModel.onEvent(SignupUIEvent.RegisterButtonClicked)
                                },
                                isEnabled = signupViewModel.allValidationsPassed.value
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    DividerTextComponent()

                    Spacer(modifier = Modifier.height(10.dp))

                    ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {
                        navController?.navigate("login")
                    }, context = context)

                }
                }
            }
            if (signupViewModel.signUpInProgress.value) {
                CircularProgressIndicator(color = SkyBlue, modifier = Modifier.size(10.dp))
            }
        }
    }

//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//
//        Surface(
//            color = Color.White,
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White)
//                .padding(28.dp)
//        ) {
//            Column(modifier = Modifier.fillMaxSize()) {
//                HeadingTextComponent(value = stringResource(id = create_account))
//                Spacer(modifier = Modifier.height(20.dp))
//                TextFieldComponent(
//                    labelValue = stringResource(id = firstname),
//                    painterResource(id = R.drawable.profile),
//                    onTextChanged = {
//                        signupViewModel.onEvent(SignupUIEvent.FirstNameChanged(it))
//                    },
//                    errorStatus = signupViewModel.registrationUIState.value.firstNameError
//                )
//                TextFieldComponent(
//                    labelValue = stringResource(id = lastname),
//                    painterResource(id = R.drawable.profile),
//                    onTextChanged = {
//                        signupViewModel.onEvent(SignupUIEvent.LastNameChanged(it))
//                    },
//                    errorStatus = signupViewModel.registrationUIState.value.lastNameError
//                )
//                TextFieldComponent(
//                    labelValue = stringResource(id = email),
//                    painterResource(id = R.drawable.email),
//                    onTextChanged = {
//                        signupViewModel.onEvent(SignupUIEvent.EmailChanged(it))
//                    },
//                    errorStatus = signupViewModel.registrationUIState.value.emailError
//                )
//                PasswordTextFieldComponent(
//                    labelValue = stringResource(id = password),
//                    painterResource(id = R.drawable.password),
//                    onTextSelected = {
//                        signupViewModel.onEvent(SignupUIEvent.PasswordChanged(it))
//                    },
//                    errorStatus = signupViewModel.registrationUIState.value.passwordError
//                )
//                CheckboxComponent(
//                    value = stringResource(id = terms_and_conditions),
//                    onTextSelected = {
//                        navController?.navigate("termsAndConditionsScreen")
//                    },
//                    onCheckedChange = {
//                        signupViewModel.onEvent(SignupUIEvent.PrivacyPolicyCheckBoxClicked(it))
//                    }
//                )
//                Spacer(modifier = Modifier.height(40.dp))
//                ButtonComponent(
//                    value = stringResource(id = register),
//                    onButtonClicked = {
//                        signupViewModel.onEvent(SignupUIEvent.RegisterButtonClicked)
//                    },
//                    isEnabled = signupViewModel.allValidationsPassed.value
//                )
//                Spacer(modifier = Modifier.height(20.dp))
//
//                DividerTextComponent()
//
//                ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {
//                    navController?.navigate("login") }, context = context)
//            }
//
//
//        }
//        if (signupViewModel.signUpInProgress.value) {
//            CircularProgressIndicator(color = SkyBlue)
//        }
//    }
}


@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}