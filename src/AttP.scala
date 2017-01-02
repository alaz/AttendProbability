object AttP {
  def calculate(probabilities: List[Double], needed: Int): Double = {
    val N = probabilities.length

    // probability that a given set of persons attends
    def P(att: Set[Int]) =
      probabilities.zipWithIndex.map { case (p,i) =>
        if (att contains i) p
        else 1.0-p
      }.product

    // from `needed` up to `N` could be attending
    (needed to N).map { attN =>
      // attN is the exact number of attendees
      (0 until N).combinations(attN).map(_.toSet).map(P).sum
    }.sum
  }
}
