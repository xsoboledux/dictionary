package ru.xsobolx.dictionary.data.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.xsobolx.dictionary.data.db.base.AppDatabase
import ru.xsobolx.dictionary.data.db.translation.dao.TranslationDAO
import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel

@RunWith(AndroidJUnit4ClassRunner::class)
class TranslationDAOTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var translationDAO: TranslationDAO
    private lateinit var testSubscriber: TestObserver<DictionaryDBModel>
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        testSubscriber = TestObserver()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        translationDAO = db.translationDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getAllEntriesAfterInsert() {
        translationDAO.insertDictionaryEntry(testDictionaryDataBaseModel1)
            .subscribe(testSubscriber)
        testSubscriber.assertComplete()
        translationDAO.insertDictionaryEntry(testDictionaryDataBaseModel2)
            .subscribe(testSubscriber)
        testSubscriber.assertComplete()
        translationDAO.insertDictionaryEntry(testDictionaryDataBaseModel3)
            .subscribe(testSubscriber)
        testSubscriber.assertComplete()

        val nSub = TestObserver<List<DictionaryDBModel>>()
        translationDAO.loadAllDictionaryEntries()
            .subscribe(nSub)

        nSub.assertResult(
            listOf(
                testDictionaryDataBaseModel1,
                testDictionaryDataBaseModel2,
                testDictionaryDataBaseModel3
            )
        )
    }
}
