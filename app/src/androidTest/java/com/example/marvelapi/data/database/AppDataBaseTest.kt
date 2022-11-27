package com.example.marvelapi.data.database


import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marvelapi.data.repository.LocalFavoritesCharactersRepository
import com.example.marvelapi.ui.home.HomeViewModel
import junit.framework.Assert.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import java.io.IOException

/**
 *
 *
 *
 * created on 27/11/2022
 * @author Lucas Goncalves
 */
@RunWith(AndroidJUnit4::class)
class AppDataBaseTest {

    companion object{
        const val CHARACTER_ID = 11111L
    }
    @Mock
    private lateinit var viewModel: HomeViewModel
    private lateinit var localFavoritesCharactersRepository: LocalFavoritesCharactersRepository

    private lateinit var favDao: FavoriteCharacterDao
    private lateinit var db: AppDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val favoriteCharacter = FavoriteCharacterInterface(
        id = CHARACTER_ID,
        name = "Spider-Man",
        description = "Description Test",
        imagePath = null,
    )

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).allowMainThreadQueries().build()
        favDao = db.favoriteCharacterDao()
        localFavoritesCharactersRepository = LocalFavoritesCharactersRepository(favDao)
        viewModel = HomeViewModel(localFavoritesCharactersRepository)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun getCharacterByIdFromDatabase() = runBlocking {
        favDao.insert(favoriteCharacter)
        assertEquals(favDao.getById(CHARACTER_ID.toString()), favoriteCharacter)
    }

    @Test
    fun getCharacterThatNotExistsByIdFromDatabase() = runBlocking {
        val favoriteCharacter = FavoriteCharacterInterface(
            id = 11112,
            name = "Spider-Man",
            description = "Description Test",
            imagePath = null,
        )

        favDao.insert(favoriteCharacter)
        assertNotSame(favDao.getById(CHARACTER_ID.toString()), favoriteCharacter)

    }

    @Test
    fun checkIfInsertedCharacterInDatabaseExists() = runBlocking {
        val favoriteCharacter = FavoriteCharacterInterface(
            id = CHARACTER_ID,
            name = "Spider-Man",
            description = "Description Test",
            imagePath = null,
        )

        favDao.insert(favoriteCharacter)
        assertEquals(favDao.getAll().first()[0], favoriteCharacter)
    }

    @Test
    fun checkIfHasNoCharacterInDatabase() = runBlocking {
        //favDao.insert(favoriteCharacter)
        assertEquals(favDao.getAll().first(), emptyList<Any>())
    }

}