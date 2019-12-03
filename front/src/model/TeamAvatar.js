
import angela from './../assets/angela.jpg'
import broseph from './../assets/broseph.jpeg'
import che from './../assets/che.png'
import karl from './../assets/karl.jpg'
import lenin from './../assets/lenin.jpg'
import mao from './../assets/mao.jpg'
import sankara from './../assets/sankara.jpg'
import trotsky from './../assets/trotsky.jpg'
import engels from './../assets/engels.jpg'
import gandi from './../assets/gandi.jpg'
import martin from './../assets/martin.jpg'

var avatars = [angela, broseph, che, karl, lenin, mao, sankara, trotsky, engels, gandi, martin]

class TeamAvatar {
    constructor(id) {
        this.id = id
        this.avatar = avatars.pop()
    }
}

export default TeamAvatar