package com.merxury.blocker.ui.component

import android.content.pm.ComponentInfo
import com.merxury.blocker.core.IController
import com.merxury.blocker.ui.base.BasePresenter
import com.merxury.blocker.ui.base.BaseView
import com.merxury.blocker.ui.strategy.entity.view.AppComponentInfo

/**
 * Created by Mercury on 2018/3/18.
 */
interface ComponentContract {
    interface View : BaseView<Presenter> {
        fun showComponentList(components: List<ComponentInfo>)
        fun setLoadingIndicator(active: Boolean)
        fun showNoComponent()
        fun searchForComponent(name: String)
        fun showFilteringPopUpMenu()
        fun showAlertDialog()
        fun refreshComponentState(componentName: String)
        fun showAddComment(component: ComponentInfo)
        fun showVoteFail()
    }

    interface Presenter : BasePresenter, IController {
        var currentComparator: EComponentComparatorType
        fun loadComponents(packageName: String, type: EComponentType)
        fun sortComponentList(components: List<ComponentInfo>, type: EComponentComparatorType): List<ComponentInfo>
        fun checkComponentIsVoted(component: ComponentInfo): Boolean
        fun voteForComponent(component: ComponentInfo, type: EComponentType)
        fun downVoteForComponent(component: ComponentInfo, type: EComponentType)
        fun writeComponentVoteState(component: ComponentInfo, like: Boolean)
        fun addToIFW(component: ComponentInfo, type: EComponentType)
        fun removeFromIFW(component: ComponentInfo, type: EComponentType)
        fun launchActivity(component: ComponentInfo)
    }

    interface ComponentDataPresenter : BasePresenter {
        val packageName: String
        fun getComponentData(): AppComponentInfo
        fun loadComponentData()
        fun refreshComponentData()
        fun sendDescription(component: ComponentInfo, type: EComponentType, description: String)
    }

    interface ComponentMainView {
        fun onComponentLoaded(appComponentInfo: AppComponentInfo)
        fun getComponentDataPresenter(): ComponentDataPresenter
    }

    interface ComponentItemListener {
        fun onComponentClick(component: ComponentInfo)
        fun onComponentLongClick(component: ComponentInfo)
        fun onSwitchClick(component: ComponentInfo, isChecked: Boolean)
        fun onUpVoteClick(component: ComponentInfo)
        fun onDownVoteClick(component: ComponentInfo)
    }
}