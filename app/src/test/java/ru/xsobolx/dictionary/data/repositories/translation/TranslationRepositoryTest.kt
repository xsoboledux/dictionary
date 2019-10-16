package ru.xsobolx.dictionary.data.repositories.translation

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ru.xsobolx.dictionary.data.db.translation.dao.TranslationDAO
import ru.xsobolx.dictionary.data.db.translation.mapper.DictionaryDataBaseToDomainModelMapper
import ru.xsobolx.dictionary.data.db.translation.mapper.DictionaryDomainToDataBaseModelMapper
import ru.xsobolx.dictionary.data.network.translation.TranslationApi
import ru.xsobolx.dictionary.data.network.translation.mapper.TranslationApiMapper
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.testDictionaryDataBaseModel
import ru.xsobolx.dictionary.domain.translation.testEntry
import ru.xsobolx.dictionary.domain.translation.testTranslatedWord
import ru.xsobolx.dictionary.domain.translation.testTranslationResponse

class TranslationRepositoryTest {
    @Mock
    private lateinit var translationDAO: TranslationDAO
    @Mock
    private lateinit var dictionaryDataBaseToDomainModelMapper: DictionaryDataBaseToDomainModelMapper
    @Mock
    private lateinit var dictionaryDomainToDataBaseModelMapper: DictionaryDomainToDataBaseModelMapper
    @Mock
    private lateinit var translationApi: TranslationApi
    @Mock
    private lateinit var translationApiMapper: TranslationApiMapper
    private lateinit var testSubscriber: TestObserver<DictionaryEntry>
    private lateinit var translationRepository: TranslationRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testSubscriber = TestObserver()
        translationRepository = TranslationRepository.Impl(
            translationDAO,
            dictionaryDataBaseToDomainModelMapper,
            dictionaryDomainToDataBaseModelMapper,
            translationApi,
            translationApiMapper
        )
    }

    @Test
    fun shouldReturnDictionaryEntryFromApiIfIsNotInDBAndInsertToDb() {
        `when`(translationDAO.findTranslation("test")).thenReturn(Maybe.empty())
        `when`(translationApi.translate(testTranslatedWord.word, "en-ru")).thenReturn(
            Single.just(
                testTranslationResponse
            )
        )
        `when`(translationApiMapper.map(testTranslationResponse)).thenReturn(testEntry)
        `when`(dictionaryDomainToDataBaseModelMapper.map(testEntry)).thenReturn(
            testDictionaryDataBaseModel
        )

        val actual = translationRepository.getTranslation(testTranslatedWord)
        actual.subscribe(testSubscriber)

        verify(translationDAO, times(1)).findTranslation("test")
        verify(translationApi, times(1)).translate(testTranslatedWord.word, "en-ru")
        verify(translationApiMapper, times(1)).map(testTranslationResponse)
        verify(dictionaryDomainToDataBaseModelMapper, times(1)).map(testEntry)
        verify(translationDAO, times(1)).insertDictionaryEntry(testDictionaryDataBaseModel)
        verifyNoMoreInteractions(translationApi)
        verifyNoMoreInteractions(translationApiMapper)
        verifyNoMoreInteractions(dictionaryDomainToDataBaseModelMapper)
        verifyNoMoreInteractions(translationDAO)
        verifyZeroInteractions(dictionaryDataBaseToDomainModelMapper)

        testSubscriber.assertComplete()
        testSubscriber.assertNoErrors()
        testSubscriber.assertResult(testEntry)
    }

    @Test
    fun shouldReturnDictionaryEntryFromDBAndDoNotInvokeApi() {
        `when`(translationDAO.findTranslation("test")).thenReturn(
            Maybe.just(
                listOf(
                    testDictionaryDataBaseModel
                )
            )
        )
        `when`(dictionaryDataBaseToDomainModelMapper.map(testDictionaryDataBaseModel)).thenReturn(
            testEntry
        )
        `when`(translationApi.translate(testTranslatedWord.word, "en-ru")).thenReturn(
            Single.just(
                testTranslationResponse
            )
        )
        `when`(translationApiMapper.map(testTranslationResponse)).thenReturn(testEntry)


        val actual = translationRepository.getTranslation(testTranslatedWord)
        actual.subscribe(testSubscriber)

        verify(translationDAO, times(1)).findTranslation("test")
        verify(dictionaryDataBaseToDomainModelMapper, times(1)).map(testDictionaryDataBaseModel)
        verifyZeroInteractions(translationApi)
        verifyNoMoreInteractions(translationDAO)
        verifyNoMoreInteractions(dictionaryDataBaseToDomainModelMapper)

        testSubscriber.assertComplete()
        testSubscriber.assertNoErrors()
        testSubscriber.assertResult(testEntry)
    }

    @Test
    fun shouldReturnDictionaryEntryOnSearch() {
        `when`(translationDAO.findTranslation("test")).thenReturn(
            Maybe.just(
                listOf(
                    testDictionaryDataBaseModel
                )
            )
        )
        `when`(dictionaryDataBaseToDomainModelMapper.map(testDictionaryDataBaseModel)).thenReturn(
            testEntry
        )

        val actual = translationRepository.search("test")
        val listSubscriber = TestObserver<List<DictionaryEntry>>()
        actual.subscribe(listSubscriber)

        verify(translationDAO, times(1)).findTranslation("test")
        verify(dictionaryDataBaseToDomainModelMapper, times(1)).map(testDictionaryDataBaseModel)
        verifyNoMoreInteractions(translationDAO)
        verifyNoMoreInteractions(dictionaryDataBaseToDomainModelMapper)
        verifyZeroInteractions(translationApiMapper)
        verifyZeroInteractions(translationApi)

        listSubscriber.assertResult(listOf(testEntry))
    }

    @Test
    fun shouldReturnListOfDictionaryEntriesOnGetAllTranslations() {
        `when`(translationDAO.loadAllDictionaryEntries()).thenReturn(
            Single.just(
                listOf(
                    testDictionaryDataBaseModel
                )
            )
        )
        `when`(dictionaryDataBaseToDomainModelMapper.map(testDictionaryDataBaseModel)).thenReturn(
            testEntry
        )

        val actual = translationRepository.getAllSavedTranslations()
        val testDictioanryListSubscriber = TestObserver<List<DictionaryEntry>>()
        actual.subscribe(testDictioanryListSubscriber)

        verify(translationDAO, times(1)).loadAllDictionaryEntries()
        verify(dictionaryDataBaseToDomainModelMapper, times(1)).map(testDictionaryDataBaseModel)
        verifyNoMoreInteractions(translationDAO)
        verifyNoMoreInteractions(dictionaryDataBaseToDomainModelMapper)
        verifyZeroInteractions(translationApi)
        verifyZeroInteractions(dictionaryDomainToDataBaseModelMapper)
        verifyZeroInteractions(translationApiMapper)

        testDictioanryListSubscriber.assertComplete()
        testDictioanryListSubscriber.assertNoErrors()
        testDictioanryListSubscriber.assertResult(listOf(testEntry))
    }
}