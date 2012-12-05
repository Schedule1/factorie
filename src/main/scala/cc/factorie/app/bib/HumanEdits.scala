package cc.factorie.app.bib
import cc.factorie.util.Attr
import cc.factorie._
import cc.factorie.app.nlp.coref._

/* Keep track of one of these per user */
class UserReliabilityVariable extends RealVariable with EntityAttr {
  var totalImpactfulEdits = 0.0
  var totalEdits = 0.0
  def updateValue(d:DiffLis) = this.set(totalImpactfulEdits/totalEdits)(d)
}

trait HumanEditMention extends Attr{
  def editSet = attr[EditSetVariable]
  //var linkSet
  var editType:String = "none"
  attr += new EditSetVariable
}

class EditSetVariable(val entity: Entity) extends SetVariable[HierEntity with HumanEditMention] with EntityAttr

class HumanEditTemplate extends TupleTemplateWithStatistics4[EntityExists, IsEntity, EditSetVariable,UserReliabilityVariable] {

  def unroll1(eExists: EntityExists) = Factor(eExists,
    eExists.entity.attr[IsEntity], eExists.entity.attr[EditSetVariable],
    eExists.entity.attr[UserReliabilityVariable])

  def unroll2(isEntity: IsEntity) = Factor(isEntity.entity.attr[EntityExists],
    isEntity, isEntity.entity.attr[EditSetVariable],
    isEntity.entity.attr[UserReliabilityVariable])

  def unroll3(editSetVar: EditSetVariable) = Factor(
    editSetVar.entity.attr[EntityExists], editSetVar.entity.attr[IsEntity]
    editSetVar, editSetVar.entity.attr[UserReliabilityVariable])

  def unroll4(reliability: UserReliabilityVariable) = Factor(
    reliability.entity.attr[EntityExists], reliability.entity.attr[IsEntity],
    reliability.entity.attr[EditSetVar], reliability)

  def score(eExists: EntityExists#Value, isEntity: IsEntity#Value, editSetVar:
  EditSetVariable#Value, reliability: UserReliabilityVariable#Value):
  Double = {

    var result = 0.0
    for (edit <- editSetVar) {
      result += scoreEdit(edit)
    }

    //if(edit.entityRoot eq edit.linkedEdit.entityRoot)
    //if(e1.entityRoot eq e2.entityRoot) then they are in the same entity
    result
  }
}
