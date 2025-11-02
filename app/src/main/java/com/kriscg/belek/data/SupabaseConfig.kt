package com.kriscg.belek.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.realtime.Realtime

object SupabaseConfig {
    private const val SUPABASE_URL = "https://wmjzgdyhmbgpqwsksktz.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndtanpnZHlobWJncHF3c2tza3R6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk5NjIxNzcsImV4cCI6MjA3NTUzODE3N30.lgj4Y7XeCN56eY2kG2cCUMqtoypuBoCj-hc9vcefvIE"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Auth)
        install(Realtime)
    }
}