package com.merxury.blocker.ui.detail.component

import android.content.Context
import android.content.pm.ComponentInfo
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import com.merxury.blocker.core.ComponentControllerProxy
import com.merxury.blocker.core.root.EControllerMethod
import com.merxury.ifw.IntentFirewallImpl
import com.merxury.libkit.entity.getSimpleName
import com.merxury.libkit.utils.ApplicationUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComponentViewModel(private val pm: PackageManager) : ViewModel() {
    private val logger = XLog.tag("ComponentViewModel")

    private val _services = MutableLiveData<List<ComponentData>>()
    val services: LiveData<List<ComponentData>>
        get() = _services
    private val _receivers = MutableLiveData<List<ComponentData>>()
    val receivers: LiveData<List<ComponentData>>
        get() = _receivers
    private val _activities = MutableLiveData<List<ComponentData>>()
    val activities: LiveData<List<ComponentData>>
        get() = _activities
    private val _providers = MutableLiveData<List<ComponentData>>()
    val providers: LiveData<List<ComponentData>>
        get() = _providers

    fun load(context: Context, packageName: String, type: EComponentType) {
        viewModelScope.launch {
            val components = getComponents(packageName, type)
        }
    }

    private fun convertToComponentData(
        context: Context,
        packageName: String,
        components: MutableList<out ComponentInfo>
    ): MutableList<ComponentData> {
        val ifwController = IntentFirewallImpl.getInstance(context, packageName)
        val pmController = ComponentControllerProxy.getInstance(EControllerMethod.PM, context)
        return components.map {
            ComponentData(
                name = it.name,
                simpleName = it.getSimpleName(),
                packageName = it.packageName,
                ifwBlocked = !ifwController.getComponentEnableState(packageName, it.name),
                pmBlocked = !pmController.checkComponentEnableState(packageName, it.name),
                // Check is running
            )
        }.toMutableList()
    }

    private suspend fun getComponents(
        packageName: String,
        type: EComponentType,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): MutableList<out ComponentInfo> {
        return withContext(dispatcher) {
            val components = when (type) {
                EComponentType.RECEIVER -> ApplicationUtil.getReceiverList(pm, packageName)
                EComponentType.ACTIVITY -> ApplicationUtil.getActivityList(pm, packageName)
                EComponentType.SERVICE -> ApplicationUtil.getServiceList(pm, packageName)
                EComponentType.PROVIDER -> ApplicationUtil.getProviderList(pm, packageName)
            }
            return@withContext components.asSequence().sortedBy { it.getSimpleName() }
                .toMutableList()
        }
    }
}