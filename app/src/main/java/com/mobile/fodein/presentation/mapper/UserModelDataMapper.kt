package com.mobile.fodein.presentation.mapper

import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.models.data.User
import com.mobile.fodein.presentation.model.FormModel
import com.mobile.fodein.presentation.model.UserModel


class UserModelDataMapper {

    private val context = App.appComponent.context()
    private val formModelDataMapper:
            FormModelDataMapper = FormModelDataMapper()

    fun transform(user: User?): UserModel{
        if (user == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val userModel = UserModel()
        userModel.id = user.id
        userModel.address = user.address
        userModel.description = user.description
        userModel.email = user.email
        userModel.idCard = user.idCard
        userModel.name = user.name
        userModel.password = user.password
        userModel.phone = user.phone
        userModel.roll = user.roll
        userModel.unit = user.unit
        userModel.user = user.user
        val formModelCollection: Collection<FormModel> = this
                .formModelDataMapper
                .transform(user.forms)
        userModel.list = formModelCollection as List<FormModel>
        return userModel
    }

    fun transform(usersCollection: Collection<User>?): Collection<UserModel>{
        val usersModelCollection: MutableCollection<UserModel> = ArrayList()

        if (usersCollection != null && !usersCollection.isEmpty())
            usersCollection.mapTo(usersModelCollection) { transform(it) }
        return usersModelCollection
    }
}