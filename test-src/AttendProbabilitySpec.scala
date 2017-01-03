import org.scalacheck.{Arbitrary, Gen, Shrink}
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.prop.PropertyChecks
import org.scalatest.prop.GeneratorDrivenPropertyChecks.PropertyCheckConfiguration

class AttendProbabilitySpec extends FunSpec with Matchers with PropertyChecks {
  describe("For two visitors, p=0.5 every, the result") {
    val eachP = 0.5
    val personsP = List(eachP, eachP)

    it("equals 0.25 when 2 needed") {
      AttendProbability.calculate(personsP, 2) should equal(eachP*eachP)
    }

    it("equals 0.75 when at least 1 needed") {
      // either 1 or 2
      AttendProbability.calculate(personsP, 1) should equal(eachP*eachP + eachP)
    }
  }


  implicit override val generatorDrivenConfig =
    PropertyCheckConfiguration(minSize = 1, sizeRange = 14)

  describe("For arbitrary data, ") {
    val probability = Gen.chooseNum[Double](0.0, 1.0)

    it("the P of no-one attends equals 1 minus `atLeast(1)`") {
      forAll(Gen.nonEmptyListOf(probability) :| "probabilities") { probabilities =>
        val p = new AttendProbability(probabilities)
        p.exact(0)+p.atLeast(1) should equal(1.0 +- .000001)
      }
    }

    it("the P of exactly 1 equals its probability, when the list contains only 1") {
      forAll(Gen.listOfN(1, probability) :| "probabilities") { probabilities =>
        val p = new AttendProbability(probabilities)
        p.exact(1) should equal(probabilities(0))
      }
    }

    it("the net probability of all variants (either `atMost` or `atLeast`) should be 1") {
      forAll(Gen.nonEmptyListOf(probability) :| "probabilities") { probabilities =>
        val p = new AttendProbability(probabilities)
        forAll(Gen.choose(0,probabilities.length) :| "n") { n =>
          p.atMost(n)+p.atLeast(n) should equal(1.0 +- .000001)
        }
      }
    }
  }
}
