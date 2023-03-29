package com.example.posapp.PasswordUtils

import org.mindrot.jbcrypt.BCrypt

object PasswordUtils {

    // Mã hóa
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    // So sánh mật khẩu
    fun checkPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }
}
