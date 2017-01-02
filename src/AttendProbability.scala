class AttendProbability(personsP: List[Double]) {
  val N = personsP.length

  // precalculate the probabilities of attend/not-attend per person
  // (we want fast lookup by index)
  val pYes = personsP.toIndexedSeq
  val pNo = personsP.map(1.0.-).toIndexedSeq

  // probability that a given set of persons attends
  def P(att: Set[Int]) =
    (0 until N).map { i =>
      if (att contains i) pYes(i)
      else pNo(i)
    }.product

  // probability of exact `n` attendees
  def exact(n: Int) =
    (0 until N).combinations(n).map(_.toSet).map(P).sum

  // probability at least `n` attendees (=inclusive)
  def atLeast(n: Int) =
    (n to N).map(exact).sum

  // probability less than `n` attendees (=exclusive)
  def atMost(n: Int) =
    (0 until n).map(exact).sum
}

object AttendProbability {
  def calculate(probabilities: List[Double], needed: Int): Double = {
    val probability = new AttendProbability(probabilities)
    probability.atLeast(needed)
  }
}
