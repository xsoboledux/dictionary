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
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry

@RunWith(AndroidJUnit4ClassRunner::class)
class TranslationDAOTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var translationDAO: TranslationDAO
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
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
        translationDAO.insertDictionaryEntry(testDictionaryDataBaseModel2)
        translationDAO.insertDictionaryEntry(testDictionaryDataBaseModel3)

        val loadAllSubscriber = TestObserver<List<DictionaryDBModel>>()
        translationDAO.loadAllDictionaryEntries()
            .subscribe(loadAllSubscriber)

        loadAllSubscriber.assertResult(
            listOf(
                testDictionaryDataBaseModel1,
                testDictionaryDataBaseModel2,
                testDictionaryDataBaseModel3
            )
        )
    }

    @Test
    fun shouldUpdateDictionaryEntry() {
        val oldDictionaryEntry = testDictionaryDataBaseModel1
        translationDAO.insertDictionaryEntry(oldDictionaryEntry)
        val loadAllOldDictionaryEntriesSubscriber = TestObserver<List<DictionaryDBModel>>()
        translationDAO.loadAllDictionaryEntries()
            .subscribe(loadAllOldDictionaryEntriesSubscriber)
        loadAllOldDictionaryEntriesSubscriber.assertResult(listOf(oldDictionaryEntry))

        val updateSubscriber = TestObserver<DictionaryDBModel>()
        translationDAO.updateDictionaryEntry("test1", true)
            .subscribe(updateSubscriber)
        updateSubscriber.assertComplete()

        val newEntry = testDictionaryDataBaseModel1.copy(isFavorite = true)
        val newEntriesSubscriber = TestObserver<List<DictionaryDBModel>>()
        translationDAO.loadAllDictionaryEntries()
            .subscribe(newEntriesSubscriber)
        newEntriesSubscriber.assertResult(listOf(newEntry))
    }

    @Test
    fun testSearch() {
        translationDAO.insertDictionaryEntry(testDictionaryDataBaseModel1)
        translationDAO.insertDictionaryEntry(testDictionaryDataBaseModel2)
        translationDAO.insertDictionaryEntry(testDictionaryDataBaseModel3)

        val expected = testDictionaryDataBaseModel3
        val searchSubscriber = TestObserver<List<DictionaryDBModel>>()
        translationDAO.searchTranslation("test3")
            .subscribe(searchSubscriber)
        searchSubscriber.assertResult(listOf(expected))
    }
}
