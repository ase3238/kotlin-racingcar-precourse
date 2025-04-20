import entity.Car
import model.RaceModel
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.collections.listOf

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
            TestCase(
                "on success", "a,b,c", false,
                listOf(
                    Car("a",0),
                    Car("b",0),
                    Car("c",0),
                ),
                null
            ),
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

        @JvmStatic
        fun winnerTestCases() = listOf(
            TestCase(
                "one winner",
                listOf(
                    Car("a",3),
                    Car("b",5),
                    Car("c",2),
                ),
                false, listOf("b"), null
            ),
            TestCase(
                "multiple winner",listOf(
                    Car("a",3),
                    Car("b",5),
                    Car("c",2),
                    Car("d",5),
                ),
                false, listOf("b", "d"), null
            ),
            TestCase("empty list", emptyList<Car>(), true, null, "자동차가 없습니다."),
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

    @ParameterizedTest(name = "{0}")
    @MethodSource("winnerTestCases")
    fun testGetWinner(testCase: TestCase) {
        model.carList = testCase.input as List<Car>
        if (testCase.expectError) {
            assertThatThrownBy { model.getWinners() }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining(testCase.expectErrorMsg)
        } else {
            assertThat(model.getWinners())
                .isEqualTo(testCase.expectResult)
        }
    }
}