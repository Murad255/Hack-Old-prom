package com.example.mqttkotlinsample.data

import android.app.KeyguardManager
import android.content.Context
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import java.io.IOException
import java.security.cert.CertificateException
import android.security.keystore.KeyProperties
import android.security.keystore.KeyGenParameterSpec
import java.security.*
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException


class FingerPrint {

    private lateinit var KEY_ALIAS: String

    //Совместимость можно легко проверить с помощью метода
    fun checkFingerprintCompatibility(context: Context): Boolean {
        return FingerprintManagerCompat.from(context).isHardwareDetected
    }


    enum class SensorState {
        NOT_SUPPORTED, NOT_BLOCKED,  // если устройство не защищено пином, рисунком или паролем
        NO_FINGERPRINTS,  // если на устройстве нет отпечатков
        READY
    }

    //готов ли сенсор к использованию
    fun checkSensorState(context: Context): SensorState? {
        return if (checkFingerprintCompatibility(context)) {
            val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (!keyguardManager.isKeyguardSecure) {
                return SensorState.NOT_BLOCKED
            }
            val fingerprintManager = FingerprintManagerCompat.from(context)
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                SensorState.NO_FINGERPRINTS
            } else SensorState.READY
        } else {
            SensorState.NOT_SUPPORTED
        }
    }

    private var sKeyStore: KeyStore? = null
    private fun getKeyStore(): Boolean {
        try {
            sKeyStore = KeyStore.getInstance("AndroidKeyStore")
            KeyStore.getInstance("AndroidKeyStore").load(null)
            return true
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        }
        return false
    }


//    private var sKeyPairGenerator: KeyPairGenerator? = null
//    private fun getKeyPairGenerator(): Boolean {
//        try {
//            sKeyPairGenerator =
//                KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
//            return true
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        } catch (e: NoSuchProviderException) {
//            e.printStackTrace()
//        }
//        return false
//    }
//
//    private fun generateNewKey(): Boolean {
//        if (getKeyPairGenerator()) {
//            try {
//                sKeyPairGenerator.initialize(
//                    KeyGenParameterSpec.Builder(
//                        KEY_ALIAS,
//                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
//                    )
//                        .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
//                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
//                        .setUserAuthenticationRequired(true)
//                        .build()
//                )
//                sKeyPairGenerator.generateKeyPair()
//                return true
//            } catch (e: InvalidAlgorithmParameterException) {
//                e.printStackTrace()
//            }
//        }
//        return false
//    }
//
//    private fun isKeyReady(): Boolean {
//        try {
//            return sKeyStore!!.containsAlias(KEY_ALIAS) || generateNewKey()
//        } catch (e: KeyStoreException) {
//            e.printStackTrace()
//        }
//        return false
//    }
//
//    private var sCipher: Cipher? = null
//    private fun getCipher(): Boolean {
//        try {
//            sCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
//            return true
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        } catch (e: NoSuchPaddingException) {
//            e.printStackTrace()
//        }
//        return false
//    }

    /*
    * Итак, не зацикливаясь на проверке пин-кода на валидность, прикинем следующую упрощенную логику действий:

Пользователь вводит пин-код, если SensorState.READY, то мы сохраняем пин-код, запускаем MainActivity.
Рестартим приложение, если SensorState.READY, то считываем отпечаток, достаем пин-код, имитируем его ввод, запускаем MainActivity.
    *
    * */
}