import entity.Car
import model.RaceModel
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class RaceModelTest {
    private lateinit var model: RaceModel

    data class TestCase (
        val case: String,
        val input: Any,
        val expectError: Boolean,
        val expectResult: Any?,
        val expectErrorMsg: String?,
    )

    companion object {
        @JvmStatic
        fun carListTestCases() = listOf(
            TestCase("on success", "a,b", false, listOf(Car("a",0),Car("b",0)), null),
            TestCase("empty list", "", true, null, "입력된 이름이 없습니다."),
            TestCase("invalid name", "a,long_string", true, null, "자동차 이름은 5자 이하만 가능합니다.")
        )
    }

    @BeforeEach
    fun setUpTest() {
        model = RaceModel()
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("carListTestCases")
    fun testInitCarList(testCase: TestCase) {
        if (testCase.expectError) {
            assertThatThrownBy { model.initCarList(testCase.input as String) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining(testCase.expectErrorMsg)
        } else {
            model.initCarList(testCase.input as String)
            assertThat(model.carList).isEqualTo(testCase.expectResult)
        }
    }
}