import entity.Car
import model.RaceModel
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class RaceModelTest {
    private lateinit var model: RaceModel

    data class TestCase (
        val case: String,
        val input: Any?,
        val expectError: Boolean,
        val expectResult: Any?,
        val expectErrorMsg: String?,
    )

    companion object {
        @JvmStatic
        fun carListTestCases() = listOf(
            TestCase("on success", "a,b", false, listOf(Car("a",0),Car("b",0)), null),
            TestCase("empty string", "", true, null, "입력된 이름이 없습니다."),
            TestCase("invalid name", "a,long_string", true, null, "자동차 이름은 5자 이하만 가능합니다.")
        )

        @JvmStatic
        fun roundInputTestCases() = listOf(
            TestCase("on success", "5", false, 5, null),
            TestCase("empty string", "", true, null, "입력된 횟수가 없습니다."),
            TestCase("wrong integer", "-1", true, null, "1 이상의 숫자만 입력 가능합니다."),
            TestCase("wrong string", "a0", true, null, "횟수는 숫자만 입력 가능합니다."),
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

    @ParameterizedTest(name = "{0}")
    @MethodSource("roundInputTestCases")
    fun testInitRound(testCase: TestCase) {
        if (testCase.expectError) {
            assertThatThrownBy { model.initRound(testCase.input as String) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining(testCase.expectErrorMsg)
        } else {
            model.initRound(testCase.input as String)
            assertThat(model.round).isEqualTo(testCase.expectResult)
        }
    }

    @Test
    fun testRunRound() {
        // Given
        model.initCarList("a,b,c")

        val beforeRun: List<Car> = model.carList.map { it.copy() }
        model.runRound()
        model.carList.forEachIndexed { idx, (_, distance) ->
            assertThat(distance == beforeRun[idx].distance || distance == beforeRun[idx].distance + 1).isTrue()
        }
    }
}