package www.breadboy.com.lockerroom.data.source.local

import www.breadboy.com.lockerroom.data.source.AppsDataSource

/**
 * Created by N4039 on 2017-09-07.
 */
class AppsLocalDataSource : AppsDataSource {

    override fun loadApp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadApps() = RealmAppFlowable().getRealmAppFlowable()

    override fun saveApp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveApps() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteApp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteApps() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}