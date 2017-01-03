class AttendProbability(personsP: List[Double]) {
  require(personsP.nonEmpty, "The list of probabilities should not be empty")
  require(personsP.forall {x => x>=0 && x<=1.0}, "The list of probabilities should contain values [0,1]")

  val N = personsP.length

  // precalculate the probabilities of attend/not-attend per person
  // (we want fast lookup by index)
  val pYes = personsP.toArray
  val pNo = personsP.map(1.0.-).toArray

  // probability that a given set of persons attends
  // Complexity: O(n)
  def P(att: Set[Int]) =
    (0 until N).map { i =>
      if (att contains i) pYes(i)
      else pNo(i)
    }.product

  // probability of exact `n` attendees
  // Complexity:
  // * combinations is C(k,n) ~ O(n^min(k,n-k)) for n>>1
  // * every P is O(n)
  // total = O(n^(1+min(k,n-k)))
  def exact(n: Int) = {
    require(n >= 0 && n <= N, "`needed` should be from 0 to the number of probabilities")
    if (n == 0) P(Set.empty)
    else (0 until N).combinations(n).map(_.toSet).map(P).sum
  }

  // probability at least `n` attendees (=inclusive)
  // Complexity: O(n^(2+min(k,n-k)))
  def atLeast(n: Int) = {
    require(n >= 0 && n <= N, "`needed` should be from 0 to the number of probabilities")
    (n to N).map(exact).sum
  }

  // probability less than `n` attendees (=exclusive)
  def atMost(n: Int) = {
    require(n >= 0 && n <= N, "`needed` should be from 0 to the number of probabilities")
    (0 until n).map(exact).sum
  }
}

object AttendProbability {
  /** Given probabilities of each participant attending in "probabilities",
    * calculate the probability that at least "needed" participants will participate.
    *
    * The probability has to be accurate to the precision of 0.001.
    */
  def calculate(probabilities: List[Double], needed: Int): Double = {
    val probability = new AttendProbability(probabilities)
    probability.atLeast(needed)
  }
}
