package com.example.notasmazmorras

import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.data.model.local.LocalCreature
import com.example.notasmazmorras.data.model.local.LocalNote
import com.example.notasmazmorras.data.model.local.LocalObject
import com.example.notasmazmorras.data.model.local.LocalPlace
import com.example.notasmazmorras.data.model.local.LocalUser
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.RemoteCampaign
import com.example.notasmazmorras.data.model.remote.RemoteCharacter
import com.example.notasmazmorras.data.model.remote.RemoteCreature
import com.example.notasmazmorras.data.model.remote.RemoteNote
import com.example.notasmazmorras.data.model.remote.RemoteObject
import com.example.notasmazmorras.data.model.remote.RemotePlace
import com.example.notasmazmorras.data.model.remote.RemoteUser
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ModelTests {

    @Test
    fun testCampaignConversion() {
        val local : LocalCampaign = LocalCampaign(
            id = "1111111",
            name = "test",
            picture = "test"
        )
        val shouldBeRemote : RemoteCampaign = RemoteCampaign(
            id = "1111111",
            name = "test",
            picture = "test"
        )
        assertEquals(shouldBeRemote, local.toRemote())
    }

    @Test
    fun testCharacterConversion() {
        val local : LocalCharacter = LocalCharacter(
            id = "1111111",
            name = "test",
            clase = "test",
            subClase = "test",
            maxPg = 1,
            pg = 1,
            picture = "test",
            campaign = "1111111"
        )
        val shouldBeRemote : RemoteCharacter = RemoteCharacter(
            id = "1111111",
            name = "test",
            clase = "test",
            subClase = "test",
            maxPg = 1,
            pg = 1,
            picture = "test",
            campaign = "1111111",
            objects = null
        )
        assertEquals(shouldBeRemote, local.toRemote())
    }

    @Test
    fun testCreatureConversion() {
        val local : LocalCreature = LocalCreature(
            id = "1111111",
            name = "test",
            species = "test",
            picture = "test",
            campaign = "1111111"
        )
        val shouldBeRemote : RemoteCreature = RemoteCreature(
            id = "1111111",
            name = "test",
            species = "test",
            picture = "test",
            campaign = "1111111"
        )
        assertEquals(shouldBeRemote, local.toRemote())
    }

    @Test
    fun testNoteConversion() {
        val local : LocalNote = LocalNote(
            id = "1111111",
            name = "test",
            content = "test",
            isDm = true,
            isEditing = false,
            owner = "1111111"
        )
        val shouldBeRemote : RemoteNote = RemoteNote(
            id = "1111111",
            name = "test",
            content = "test",
            dm = true,
            editing = false,
            owner = "1111111"
        )
        assertEquals(shouldBeRemote, local.toRemote())
    }

    @Test
    fun testObjectConversion() {
        val local : LocalObject = LocalObject(
            id = "1111111",
            name = "test",
            cost = 1.99f,
            picture = "test",
            campaign = "1111111"
        )
        val shouldBeRemote : RemoteObject = RemoteObject(
            id = "1111111",
            name = "test",
            cost = 1.99f,
            picture = "test",
            campaign = "1111111"
        )
        assertEquals(shouldBeRemote, local.toRemote())
    }

    @Test
    fun testPlaceConversion() {
        val local : LocalPlace = LocalPlace(
            id = "1111111",
            name = "test",
            picture = "test",
            campaign = "1111111"
        )
        val shouldBeRemote : RemotePlace = RemotePlace(
            id = "1111111",
            name = "test",
            picture = "test",
            campaign = "1111111"
        )
        assertEquals(shouldBeRemote, local.toRemote())
    }

    @Test
    fun testUserConversion() {
        val local : LocalUser = LocalUser(
            email = "test",
            name = "test",
            profilePicture = "test"
        )
        val shouldBeRemote : RemoteUser = RemoteUser(
            email = "test",
            name = "test",
            profilePicture = "test"
        )
        assertEquals(shouldBeRemote, local.toRemote())
    }

}