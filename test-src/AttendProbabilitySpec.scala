import org.scalatest.{FunSpec, Matchers}

class AttendProbabilitySpec extends FunSpec with Matchers {
  describe("Attend probability for two visitors, 0.5 each") {
    val eachP = 0.5
    val personsP = List(eachP, eachP)
    it("equals 0.25 when 2 needed") {
      AttendProbability.calculate(personsP, 2) should equal(eachP*eachP)
    }
    it("equals 0.75 when 1 needed") {
      // either 1 or 2
      AttendProbability.calculate(personsP, 1) should equal(eachP*eachP + eachP)
    }
  }
}
