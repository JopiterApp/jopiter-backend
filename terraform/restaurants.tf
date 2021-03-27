resource "aws_dynamodb_table" "restaurants" {
  name = "restaurants"
  write_capacity = 5
  read_capacity = 5

  hash_key = "restaurantId"
  range_key = "sortKey"


  attribute {
    name = "restaurantId"
    type = "N"
  }

  attribute {
    name = "sortKey"
    type = "S"
  }
}
