package main

import (
	"github.com/gin-gonic/gin"
)

func main() {

	r := gin.Default()
	port := ":8081"
	InitRouter(r)

	r.Run(port) // listen and serve on 0.0.0.0:8080 (for windows "localhost:8080")
}
