object AttendProbability {
  def calculate(probabilities: List[Double], needed: Int): Double = {
    val N = probabilities.length

    // precalculate the probabilities of attend/not-attend per person
    // (we want fast lookup by index)
    val pYes = probabilities.toIndexedSeq
    val pNo = probabilities.map(1.0-).toIndexedSeq

    // probability that a given set of persons attends
    def P(att: Set[Int]) =
      (0 until N).map { i =>
        if (att contains i) pYes(i)
        else pNo(i)
      }.product

    // from `needed` up to `N` could be attending
    (needed to N).map { attN =>
      // attN is the exact number of attendees
      (0 until N).combinations(attN).map(_.toSet).map(P).sum
    }.sum
  }
}
