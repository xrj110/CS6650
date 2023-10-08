package main

import (
	"CS6650_ASS1/Controller"
	"github.com/gin-gonic/gin"
)

func InitRouter(r *gin.Engine) {

	// home page

	apiRouter := r.Group("/CS6650A1")

	// basic apis
	apiRouter.POST("/albums", Controller.Post)

	apiRouter.GET("/albums/:albumID", Controller.Get)

}
