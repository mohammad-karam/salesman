package com.example.myapplication

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class SalesmanViewModel : ViewModel() {
    private val errorMessage = mutableStateOf<String?>(null)
    val salesmen = listOf(
        Salesman("Artem Titarenko", listOf("76133")),
        Salesman("Bernd Schmitt", listOf("7619*")),
        Salesman("Chris Krapp", listOf("762*")),
        Salesman("Alex Uber", listOf("86*"))
    )

    private fun isValidPostcodeExpression(expression: String): Boolean {
        return expression.matches(Regex("^\\d{5}|\\d{1,4}\\*\$"))
    }

    private val searchQuery = MutableStateFlow("")

    val filteredSalesmen: Flow<List<Salesman>> = searchQuery
        .debounce(1000)
        .distinctUntilChanged()
        .map { query ->
            if (query.isBlank()) {
                salesmen
            } else {
                salesmen.filter { salesman ->
                    salesman.areas.any { area ->
                        area.matches(Regex(query.replace("*", ".*")))
                    }
                }
            }
        }
        .flowOn(Dispatchers.Default)

    fun setSearchQuery(query: String) {
        val trimmedQuery = query.trim()
        if (isValidPostcodeExpression(trimmedQuery)) {
            searchQuery.value = trimmedQuery
            errorMessage.value = null
        } else {
            errorMessage.value = "Invalid postcode expression. Please use a valid format."
        }
    }

    fun getErrorMessage(): State<String?> {
        return errorMessage
    }
}
