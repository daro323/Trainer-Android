package de.neofonie.meinwerder

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks

/**
 * Example local unit test, which will execute on the development machine (host).

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {

  class ExampleMockClass(private val nonNullableParam: String) {
    fun getName() = nonNullableParam
  }

  @Mock lateinit var mockClass: ExampleMockClass

  @Before fun setup() {
    initMocks(this)
  }

  @Test fun notOpenClass_ctor() {
    `when`(mockClass.getName()).thenReturn("aaaa")
    assertEquals("aaaa", mockClass.getName())
    verify(mockClass, times(1)).getName()
  }
}