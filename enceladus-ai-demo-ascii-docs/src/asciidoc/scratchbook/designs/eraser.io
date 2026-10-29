eraser.io

mode plan_users {
   id
   creation_date
   user_id
   plan_id
   card_id
   extraInfo1
   extraInfo2
   extraInfo3
   extraInfo4
   extraInfo5
}

model transactions {
   id
   user_id
   txnId
   txnDate
   txnEpoch
   amount
   mode
   apr_rate
   plan_id
}

model annual_percentage_rate {
  id
  mode
  plan_id
  apr_rate
}

model statements {
  id
  total_amount_due
  minimum_amount_due
  billing_cycle_from
  billing_cycle_to
  billing_cycle_id
  payment_id
}

model settlements {
  id
  statement_id
  payment_id
  billing_cycle_id
  card_state
  creation_date
}

model authorizations {
  id
  user_id
  card_state
  plan_id
  mode
  status
  creation_date
}

model activities {
  id,
  user_id,
  activity
  mode
  creation_date
}

model plans {
   id
   product_name
   apr_rate
}

model user_plans {
   id,
   user_id
   plan_id
   card_id
   ccc_state_id
}

model billing_cylces {
   id
   user_id
   plan_id
   from_date
   to_date
   month
   year
   grace_days
   invoice_id
}

model invoices {
   id
   user_id
   plan_id
   statement_id
   invoice_sent
   bill_date
   bill_sent_date
   creation_date
}

model payments {
   id
   user_id
   billing_cycle_id
   settlement_id
   status
   current_outstanding
}

model outstandings {
   id
   total_outstanding
   current_amount_due
   minimum_amount_due
}

model cards {
  id
  user_id
  ccc_state_id
}

model ccc_state {
   id
   cccid
   user_id
   state (lost/active/blocked/yet_to_use)
}